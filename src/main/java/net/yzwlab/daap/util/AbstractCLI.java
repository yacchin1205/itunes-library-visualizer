package net.yzwlab.daap.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import net.yzwlab.daap.AccessCode;
import net.yzwlab.daap.AccessCodeListener;
import net.yzwlab.daap.LibraryService;
import net.yzwlab.daap.impl.LibraryServiceImpl;
import net.yzwlab.daap.io.PropertiesAccessCodeRepository;

/**
 * CLIの抽象基底クラスです。
 */
public abstract class AbstractCLI implements AccessCodeListener {

	/**
	 * パラメータを定義します。
	 */
	private static final String PARAM_DEVICE_ID = "net.yzwlab.deviceId";

	/**
	 * プロパティファイルを保持します。
	 */
	private File propertiesFile;

	/**
	 * デバイス名を保持します。
	 */
	private String deviceName;

	/**
	 * 認識されているライブラリのリストを保持します。
	 */
	private List<AccessCode> libraryCodes;

	/**
	 * 選択されたかどうかを保持します。
	 */
	private boolean selected;

	/**
	 * 標準入力を保持します。
	 */
	private BufferedReader stdin;

	/**
	 * デバイスIDを保持します。
	 */
	private String deviceId;

	/**
	 * アクセスコードを保持します。
	 */
	private PropertiesAccessCodeRepository accessCodes;

	/**
	 * サービスを保持します。
	 */
	private LibraryService service;

	/**
	 * 実行プログラムを構築します。
	 * 
	 * @param propertiesFile
	 *            プロパティ。nullは不可。
	 * @param deviceName
	 *            デバイス名。nullは不可。
	 * @param deviceType
	 *            デバイスタイプ。nullは不可。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public AbstractCLI(File propertiesFile, String deviceName, String deviceType)
			throws IOException {
		if (propertiesFile == null || deviceName == null || deviceType == null) {
			throw new IllegalArgumentException();
		}
		this.propertiesFile = propertiesFile;
		this.deviceName = deviceName;
		this.libraryCodes = new ArrayList<AccessCode>();
		this.selected = false;
		this.stdin = new BufferedReader(new InputStreamReader(System.in));

		if (propertiesFile.exists()) {
			Properties props = new Properties();
			InputStream in = null;
			try {
				in = new FileInputStream(propertiesFile);
				props.load(new BufferedReader(
						new InputStreamReader(in, "utf-8")));
			} finally {
				if (in != null) {
					in.close();
					in = null;
				}
			}
			this.deviceId = props.getProperty(PARAM_DEVICE_ID);
			this.accessCodes = new PropertiesAccessCodeRepository(props);
		} else {
			this.deviceId = UUID.randomUUID().toString();
			this.accessCodes = new PropertiesAccessCodeRepository(
					new Properties());
		}
		service = new LibraryServiceImpl(deviceName, deviceType, deviceId,
				accessCodes);
		service.addAccessCodeListener(this);
	}

	@Override
	public void itunesFound(LibraryService service, AccessCode accessCode) {
		if (service == null || accessCode == null) {
			throw new IllegalArgumentException();
		}
		if (accessCode.getAddress() == null || accessCode.getCode() == null
				|| accessCode.getPort() < 0) {
			saveProperties(service);

			libraryCodes.add(accessCode);
			if (selected) {
				return;
			}
			System.out.println("あらたにライブラリが見つかりました:");
			for (int i = 0; i < libraryCodes.size(); i++) {
				AccessCode code = libraryCodes.get(i);
				System.out.println("[" + i + "] " + code.getLibraryName()
						+ "  (" + code.getAddress() + ":" + code.getPort()
						+ ")");
			}

			System.out.print("接続しますか？(インデックス/スキップする場合は何もせずENTER): ");
			try {
				String ret = stdin.readLine();
				if (ret == null) {
					return;
				}
				ret = ret.trim();
				if (ret.length() == 0) {
					return;
				}

				selected = true;
				int index = Integer.parseInt(ret);
				AccessCode targetAccessCode = libraryCodes.get(index);
				System.out.println("接続待機: " + targetAccessCode.getLibraryName()
						+ "  (" + targetAccessCode.getAddress() + ":"
						+ targetAccessCode.getPort() + ") iTunesの[デバイス]内["
						+ deviceName + "]をクリックし、[1111]を入力してください。");
				service.startPairing(targetAccessCode.getLibraryName());
			} catch (Throwable th) {
				th.printStackTrace();
			}
		} else {
			codeFound(service, accessCode);
		}
	}

	@Override
	public void accessCodeFound(LibraryService service, AccessCode accessCode) {
		if (service == null || accessCode == null) {
			throw new IllegalArgumentException();
		}
		saveProperties(service);

		codeFound(service, accessCode);
	}

	/**
	 * 破棄します。
	 * 
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public void dispose() throws IOException {
		if (service == null) {
			return;
		}
		service.dispose();
		service = null;
	}

	/**
	 * 開始します。
	 * 
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public void start() throws IOException {
		for (AccessCode knownLib : accessCodes) {
			if (knownLib.getCode() != null && knownLib.getAddress() != null
					&& knownLib.getPort() > 0 && service != null) {
				codeFound(service, knownLib);
			}
		}
		if (service == null) {
			return;
		}
		service.start();
	}

	/**
	 * アクセステストを行います。
	 * 
	 * @param service
	 *            サービス。nullは不可。
	 * @param accessCode
	 *            アクセスコード。nullは不可。
	 */
	protected abstract void codeFound(LibraryService service,
			AccessCode accessCode);

	/**
	 * プロパティを保存します。
	 * 
	 * @param service
	 *            サービス。nullは不可。
	 */
	private synchronized void saveProperties(LibraryService service) {
		OutputStream out = null;
		try {
			out = new FileOutputStream(propertiesFile);

			Writer w = new BufferedWriter(new OutputStreamWriter(out, "utf-8"));
			Properties props = accessCodes.getProperties();
			props.setProperty(PARAM_DEVICE_ID, deviceId);
			props.store(w, "");
			w.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					;
				}
				out = null;
			}
		}
	}

}
