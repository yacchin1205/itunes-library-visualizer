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
 * CLI�̒��ۊ��N���X�ł��B
 */
public abstract class AbstractCLI implements AccessCodeListener {

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_DEVICE_ID = "net.yzwlab.deviceId";

	/**
	 * �v���p�e�B�t�@�C����ێ����܂��B
	 */
	private File propertiesFile;

	/**
	 * �f�o�C�X����ێ����܂��B
	 */
	private String deviceName;

	/**
	 * �F������Ă��郉�C�u�����̃��X�g��ێ����܂��B
	 */
	private List<AccessCode> libraryCodes;

	/**
	 * �I�����ꂽ���ǂ�����ێ����܂��B
	 */
	private boolean selected;

	/**
	 * �W�����͂�ێ����܂��B
	 */
	private BufferedReader stdin;

	/**
	 * �f�o�C�XID��ێ����܂��B
	 */
	private String deviceId;

	/**
	 * �A�N�Z�X�R�[�h��ێ����܂��B
	 */
	private PropertiesAccessCodeRepository accessCodes;

	/**
	 * �T�[�r�X��ێ����܂��B
	 */
	private LibraryService service;

	/**
	 * ���s�v���O�������\�z���܂��B
	 * 
	 * @param propertiesFile
	 *            �v���p�e�B�Bnull�͕s�B
	 * @param deviceName
	 *            �f�o�C�X���Bnull�͕s�B
	 * @param deviceType
	 *            �f�o�C�X�^�C�v�Bnull�͕s�B
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
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
			System.out.println("���炽�Ƀ��C�u������������܂���:");
			for (int i = 0; i < libraryCodes.size(); i++) {
				AccessCode code = libraryCodes.get(i);
				System.out.println("[" + i + "] " + code.getLibraryName()
						+ "  (" + code.getAddress() + ":" + code.getPort()
						+ ")");
			}

			System.out.print("�ڑ����܂����H(�C���f�b�N�X/�X�L�b�v����ꍇ�͉�������ENTER): ");
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
				System.out.println("�ڑ��ҋ@: " + targetAccessCode.getLibraryName()
						+ "  (" + targetAccessCode.getAddress() + ":"
						+ targetAccessCode.getPort() + ") iTunes��[�f�o�C�X]��["
						+ deviceName + "]���N���b�N���A[1111]����͂��Ă��������B");
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
	 * �j�����܂��B
	 * 
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
	 */
	public void dispose() throws IOException {
		if (service == null) {
			return;
		}
		service.dispose();
		service = null;
	}

	/**
	 * �J�n���܂��B
	 * 
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
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
	 * �A�N�Z�X�e�X�g���s���܂��B
	 * 
	 * @param service
	 *            �T�[�r�X�Bnull�͕s�B
	 * @param accessCode
	 *            �A�N�Z�X�R�[�h�Bnull�͕s�B
	 */
	protected abstract void codeFound(LibraryService service,
			AccessCode accessCode);

	/**
	 * �v���p�e�B��ۑ����܂��B
	 * 
	 * @param service
	 *            �T�[�r�X�Bnull�͕s�B
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
