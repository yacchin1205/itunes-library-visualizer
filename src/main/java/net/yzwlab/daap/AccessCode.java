package net.yzwlab.daap;

/**
 * �A�N�Z�X�R�[�h�̒�`�ł��B
 */
public class AccessCode {

	/**
	 * ���C�u��������ێ����܂��B
	 */
	private String libraryName;

	/**
	 * �A�h���X��ێ����܂��B
	 */
	private String address;

	/**
	 * �R�[�h��ێ����܂��B
	 */
	private String code;

	/**
	 * �|�[�g��ێ����܂��B
	 */
	private int port;

	/**
	 * �\�z���܂��B
	 * 
	 * @param libraryName
	 *            ���C�u�������Bnull�͕s�B
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
	 * ���C�u���������擾���܂��B
	 * 
	 * @return ���C�u�������B
	 */
	public String getLibraryName() {
		return libraryName;
	}

	/**
	 * �A�h���X���擾���܂��B
	 * 
	 * @return �A�h���X�B
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * �R�[�h���擾���܂��B
	 * 
	 * @return �R�[�h�B
	 */
	public String getCode() {
		return code;
	}

	/**
	 * �|�[�g���擾���܂��B
	 * 
	 * @return �|�[�g�B
	 */
	public int getPort() {
		return port;
	}

	/**
	 * �A�h���X��ݒ肵�܂��B
	 * 
	 * @param address
	 *            �A�h���X�B
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * �R�[�h��ݒ肵�܂��B
	 * 
	 * @param code
	 *            �R�[�h�B
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * �|�[�g��ݒ肵�܂��B
	 * 
	 * @param port
	 *            �|�[�g�B
	 */
	public void setPort(int port) {
		this.port = port;
	}

}
