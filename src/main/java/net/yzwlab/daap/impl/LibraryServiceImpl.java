package net.yzwlab.daap.impl;

import java.io.IOException;
import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;
import javax.jmdns.ServiceTypeListener;

import net.yzwlab.daap.AccessCode;
import net.yzwlab.daap.AccessCodeListener;
import net.yzwlab.daap.AccessCodeRepository;
import net.yzwlab.daap.LibraryService;
import net.yzwlab.daap.PairingStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tunesremote.daap.Paired;
import org.tunesremote.daap.PairingServer;

/**
 * RemoteとおはなしするためのZeroConfサービスを定義します。
 */
public class LibraryServiceImpl implements ServiceListener,
		ServiceTypeListener, LibraryService {

	/**
	 * Logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(LibraryServiceImpl.class);

	public final static String TOUCH_ABLE_TYPE = "_touch-able._tcp.local.";
	public final static String HOME_SHARING_TYPE = "_home-sharing._tcp.local.";
	public final static String DACP_TYPE = "_dacp._tcp.local.";
	public final static String REMOTE_TYPE = "_touch-remote._tcp.local.";
	public final static String HOSTNAME = "tunesremote";

	/**
	 * ZeroConfサービスを保持します。
	 */
	private JmDNS zeroConf;

	/**
	 * サービス情報を定義します。
	 */
	private ServiceInfo pairservice;

	/**
	 * リスナ一覧を保持します。
	 */
	private List<AccessCodeListener> listeners;

	/**
	 * 既知のライブラリ一覧を保持します。
	 */
	private AccessCodeRepository accessCodes;

	/**
	 * 構築します。
	 * 
	 * @param deviceName
	 *            デバイス名。nullは不可。
	 * @param deviceType
	 *            デバイスタイプ。nullは不可。
	 * @param deviceId
	 *            デバイスの識別子。nullは不可。
	 * @param accessCodes
	 *            アクセスコード。nullは不可。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public LibraryServiceImpl(String deviceName, String deviceType,
			String deviceId, AccessCodeRepository accessCodes)
			throws IOException {
		if (deviceName == null || deviceType == null || deviceId == null
				|| accessCodes == null) {
			throw new IllegalArgumentException();
		}
		this.zeroConf = null;
		this.pairservice = null;
		this.listeners = new ArrayList<AccessCodeListener>();
		this.accessCodes = accessCodes;

		final HashMap<String, String> values = new HashMap<String, String>();
		values.put("DvNm", deviceName);
		values.put("RemV", "10000");
		values.put("DvTy", deviceType);
		values.put("RemN", "Java Remote");
		values.put("txtvers", "1");
		values.put("Pair", "0000000000000001");

		pairservice = ServiceInfo.create(REMOTE_TYPE, deviceId,
				PairingServer.PORT, 0, 0, values);
	}

	@Override
	public void dispose() {
		if (zeroConf == null) {
			return;
		}
		zeroConf.unregisterService(pairservice);
		try {
			zeroConf.close();
		} catch (IOException e) {
			// 無視
			;
		}
		zeroConf = null;
	}

	@Override
	public void start() throws IOException {
		logger.info("Configuring Bonjour...");
		this.zeroConf = JmDNS.create();
		zeroConf.addServiceListener(TOUCH_ABLE_TYPE, this);
		zeroConf.addServiceListener(HOME_SHARING_TYPE, this);
		zeroConf.addServiceListener(DACP_TYPE, this);
		zeroConf.addServiceTypeListener(this);
		zeroConf.registerService(pairservice);
	}

	@Override
	public synchronized void addAccessCodeListener(AccessCodeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		listeners.add(l);
	}

	@Override
	public synchronized void removeAccessCodeListener(AccessCodeListener l) {
		if (l == null) {
			throw new IllegalArgumentException();
		}
		listeners.remove(l);
	}

	@Override
	public void serviceAdded(ServiceEvent event) {
		final String name = event.getName();
		logger.debug("Event: " + name);
		notifyFound(event.getDNS(), name);
	}

	@Override
	public void serviceRemoved(ServiceEvent event) {
		;
	}

	@Override
	public void serviceResolved(ServiceEvent event) {
		logger.debug("Event: " + event.getName());
		;
	}

	@Override
	public void serviceTypeAdded(ServiceEvent event) {
		logger.debug("ServiceTypeAdded: " + event.getType());
	}

	@Override
	public void subTypeForServiceTypeAdded(ServiceEvent arg0) {

	}

	@Override
	public PairingStatus startPairing(String libraryName) {
		if (libraryName == null) {
			throw new IllegalArgumentException();
		}
		AccessCode code = findKnownAccessCode(libraryName);
		if (code == null) {
			throw new IllegalArgumentException("Unknown library: "
					+ libraryName);
		}
		final PairingServer server = new PairingServer(new PairingHandler(code));
		server.start();
		return new PairingStatus() {
			@Override
			public void cancel() {
				server.destroy();
			}
		};
	}

	/**
	 * 既知のアクセスコード情報を検索します。
	 * 
	 * @param libraryName
	 *            ライブラリ名。nullは不可。
	 * @return 既知のアクセスコード情報。見つからない場合はnull。
	 */
	private AccessCode findKnownAccessCode(String libraryName) {
		if (libraryName == null) {
			throw new IllegalArgumentException();
		}
		for (AccessCode code : accessCodes) {
			if (code.getLibraryName().equals(libraryName)) {
				return code;
			}
		}
		return null;
	}

	/**
	 * ホスト発見時の処理です。
	 * 
	 * @param zeroConf
	 *            ZeroConf。nullは不可。
	 * @param library
	 *            ライブラリ。nullは不可。
	 */
	private void notifyFound(JmDNS zeroConf, String library) {
		if (zeroConf == null || library == null) {
			throw new IllegalArgumentException();
		}
		try {
			ServiceInfo serviceInfo = zeroConf.getServiceInfo(TOUCH_ABLE_TYPE,
					library);

			if (serviceInfo == null) {
				serviceInfo = zeroConf.getServiceInfo(DACP_TYPE, library);
			}
			if (serviceInfo == null) {
				serviceInfo = zeroConf.getServiceInfo(HOME_SHARING_TYPE,
						library);
			}
			if (serviceInfo == null) {
				return;
			}

			String libraryName = serviceInfo.getName();
			byte[] data = serviceInfo.getPropertyBytes("CtlN");
			if (data != null) {
				libraryName = new String(data, "utf-8");
			}
			AccessCode accessCode = new AccessCode(libraryName);
			logger.info("Library: " + libraryName);

			AccessCode knownAccessCode = findKnownAccessCode(libraryName);
			if (knownAccessCode != null) {
				knownAccessCode.setPort(serviceInfo.getPort());
				for (Inet4Address addr : serviceInfo.getInet4Addresses()) {
					knownAccessCode.setAddress(addr.getHostAddress());
				}
				notifyITunesFound(knownAccessCode);
				return;
			}

			accessCode.setPort(serviceInfo.getPort());
			for (Inet4Address addr : serviceInfo.getInet4Addresses()) {
				accessCode.setAddress(addr.getHostAddress());
			}
			accessCodes.updateCode(accessCode);
			notifyITunesFound(accessCode);

		} catch (Exception e) {
			notifyErrorDetected(e);
		}
	}

	/**
	 * iTunesが検出されたことを通知します。
	 * 
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	private void notifyITunesFound(AccessCode accessCode) {
		if (accessCode == null) {
			throw new IllegalArgumentException();
		}
		for (AccessCodeListener l : getListeners()) {
			l.itunesFound(this, accessCode);
		}
	}

	/**
	 * アクセスコードが検出されたことを通知します。
	 * 
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	private void notifyAccessCodeFound(AccessCode accessCode) {
		if (accessCode == null) {
			throw new IllegalArgumentException();
		}
		for (AccessCodeListener l : getListeners()) {
			l.accessCodeFound(this, accessCode);
		}
	}

	/**
	 * エラーが検出されたことを通知します。
	 * 
	 * @param th
	 *            エラー。nullは不可。
	 */
	private void notifyErrorDetected(Throwable th) {
		if (th == null) {
			throw new IllegalArgumentException();
		}
		for (AccessCodeListener l : getListeners()) {
			l.errorDetected(this, th);
		}
	}

	/**
	 * リスナの一覧を取得します。
	 * 
	 * @return リスナの一覧。
	 */
	private synchronized AccessCodeListener[] getListeners() {
		return listeners.toArray(new AccessCodeListener[0]);
	}

	/**
	 * ペアリング処理のハンドラを定義します。
	 */
	private class PairingHandler implements PairingListener {

		/**
		 * アクセスコードを保持します。
		 */
		private AccessCode accessCode;

		/**
		 * 構築します。
		 * 
		 * @param accessCode
		 *            アクセスコード。nullは不可。
		 */
		public PairingHandler(AccessCode accessCode) {
			if (accessCode == null) {
				throw new IllegalArgumentException();
			}
			this.accessCode = accessCode;
		}

		@Override
		public void paired(PairingServer source, Paired paired) {
			if (source == null || paired == null) {
				throw new IllegalArgumentException();
			}
			String address = paired.getTarget().getHostAddress();
			String code = paired.getCode();

			accessCode.setAddress(address);
			accessCode.setCode(code);
			notifyAccessCodeFound(accessCode);
		}

	}

}
