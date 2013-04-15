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
	 * パラメータを定義します。
	 */
	private static final String PARAM_LIBRARY = "net.yzwlab.library.";

	/**
	 * パラメータを定義します。
	 */
	private static final String PARAM_LIBRARY_NAME = "name";

	/**
	 * パラメータを定義します。
	 */
	private static final String PARAM_LIBRARY_CODE = "code";

	/**
	 * パラメータを定義します。
	 */
	private static final String PARAM_LIBRARY_PORT = "port";

	/**
	 * パラメータを定義します。
	 */
	private static final String PARAM_LIBRARY_ADDRESS = "address";

	/**
	 * 既知のライブラリ一覧を保持します。
	 */
	private List<AccessCode> knownLibraries;

	/**
	 * 構築します。
	 * 
	 * @param props
	 *            プロパティ。nullは不可。
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
	 * プロパティを取得します。
	 * 
	 * @return プロパティ。
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
