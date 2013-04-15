package net.yzwlab.daap.io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import net.yzwlab.daap.AccessCode;
import net.yzwlab.daap.AccessCodeRepository;

public class PropertiesAccessCodeRepository implements AccessCodeRepository {

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_LIBRARY = "net.yzwlab.library.";

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_LIBRARY_NAME = "name";

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_LIBRARY_CODE = "code";

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_LIBRARY_PORT = "port";

	/**
	 * �p�����[�^���`���܂��B
	 */
	private static final String PARAM_LIBRARY_ADDRESS = "address";

	/**
	 * ���m�̃��C�u�����ꗗ��ێ����܂��B
	 */
	private List<AccessCode> knownLibraries;

	/**
	 * �\�z���܂��B
	 * 
	 * @param props
	 *            �v���p�e�B�Bnull�͕s�B
	 */
	public PropertiesAccessCodeRepository(Properties props) {
		if (props == null) {
			throw new IllegalArgumentException();
		}
		this.knownLibraries = new ArrayList<AccessCode>();
		for (int i = 0; i < Integer.MAX_VALUE - 1; i++) {
			String id = String.valueOf(i) + ".";
			String name = props.getProperty(PARAM_LIBRARY + id
					+ PARAM_LIBRARY_NAME);
			String code = props.getProperty(PARAM_LIBRARY + id
					+ PARAM_LIBRARY_CODE);
			String address = props.getProperty(PARAM_LIBRARY + id
					+ PARAM_LIBRARY_ADDRESS);
			String port = props.getProperty(PARAM_LIBRARY + id
					+ PARAM_LIBRARY_PORT);
			if (name == null) {
				break;
			}
			AccessCode accessCode = new AccessCode(name);
			accessCode.setCode(code);
			accessCode.setAddress(address);
			if (port != null) {
				accessCode.setPort(Integer.parseInt(port));
			}
			knownLibraries.add(accessCode);
		}
	}

	/**
	 * �v���p�e�B���擾���܂��B
	 * 
	 * @return �v���p�e�B�B
	 */
	public Properties getProperties() {
		Properties props = new Properties();

		List<String> names = new ArrayList<String>();
		Map<String, AccessCode> codeMap = new HashMap<String, AccessCode>();
		for (AccessCode accessCode : knownLibraries) {
			names.add(accessCode.getLibraryName());
			codeMap.put(accessCode.getLibraryName(), accessCode);
		}
		Collections.sort(names);

		int index = 0;
		for (String name : names) {
			String prefix = PARAM_LIBRARY + index + ".";
			props.setProperty(prefix + PARAM_LIBRARY_NAME, name);
			AccessCode accessCode = codeMap.get(name);
			if (accessCode.getCode() != null) {
				props.setProperty(prefix + PARAM_LIBRARY_CODE,
						accessCode.getCode());
			}
			if (accessCode.getAddress() != null) {
				props.setProperty(prefix + PARAM_LIBRARY_ADDRESS,
						accessCode.getAddress());
			}
			if (accessCode.getPort() >= 0) {
				props.setProperty(prefix + PARAM_LIBRARY_PORT,
						String.valueOf(accessCode.getPort()));
			}
			index++;
		}
		return props;
	}

	@Override
	public Iterator<AccessCode> iterator() {
		return knownLibraries.iterator();
	}

	@Override
	public void updateCode(AccessCode accessCode) {
		if (accessCode == null) {
			throw new IllegalArgumentException();
		}
		knownLibraries.add(accessCode);
	}

}
