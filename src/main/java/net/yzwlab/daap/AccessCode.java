package net.yzwlab.daap;

/**
 * アクセスコードの定義です。
 */
public class AccessCode {

	/**
	 * ライブラリ名を保持します。
	 */
	private String libraryName;

	/**
	 * アドレスを保持します。
	 */
	private String address;

	/**
	 * コードを保持します。
	 */
	private String code;

	/**
	 * ポートを保持します。
	 */
	private int port;

	/**
	 * 構築します。
	 * 
	 * @param libraryName
	 *            ライブラリ名。nullは不可。
	 */
	public AccessCode(String libraryName) {
		if (libraryName == null) {
			throw new IllegalArgumentException();
		}
		this.libraryName = libraryName;
		this.address = null;
		this.code = null;
		this.port = -1;
	}

	/**
	 * ライブラリ名を取得します。
	 * 
	 * @return ライブラリ名。
	 */
	public String getLibraryName() {
		return libraryName;
	}

	/**
	 * アドレスを取得します。
	 * 
	 * @return アドレス。
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * コードを取得します。
	 * 
	 * @return コード。
	 */
	public String getCode() {
		return code;
	}

	/**
	 * ポートを取得します。
	 * 
	 * @return ポート。
	 */
	public int getPort() {
		return port;
	}

	/**
	 * アドレスを設定します。
	 * 
	 * @param address
	 *            アドレス。
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * コードを設定します。
	 * 
	 * @param code
	 *            コード。
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * ポートを設定します。
	 * 
	 * @param port
	 *            ポート。
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
