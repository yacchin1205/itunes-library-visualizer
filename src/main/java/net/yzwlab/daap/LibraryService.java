package net.yzwlab.daap;

import java.io.IOException;

/**
 * Remote�Ƃ��͂Ȃ����邽�߂�ZeroConf�T�[�r�X�p�̃C���^�t�F�[�X���`���܂��B
 */
public interface LibraryService {

	/**
	 * �J�n���܂��B
	 * 
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
	 */
	public void start() throws IOException;

	/**
	 * �j�����܂��B
	 */
	public void dispose();

	/**
	 * ���X�i��ǉ����܂��B
	 * 
	 * @param l
	 *            ���X�i�Bnull�͕s�B
	 */
	public void addAccessCodeListener(AccessCodeListener l);

	/**
	 * ���X�i���폜���܂��B
	 * 
	 * @param l
	 *            ���X�i�Bnull�͕s�B
	 */
	public void removeAccessCodeListener(AccessCodeListener l);

	/**
	 * �y�A�����O���J�n���܂��B
	 * 
	 * @param libraryName
	 *            ���C�u�������Bnull�͕s�B
	 */
	public PairingStatus startPairing(String libraryName);

	/**
	 * �Z�b�V�������J���܂��B
	 * 
	 * @param target
	 *            �ΏۃZ�b�V�����Bnull�͕s�B
	 * @return �Z�b�V�����B
	 * @throws IOException
	 *             ���o�͊֌W�̃G���[�B
	 */
	public LibrarySession openSession(AccessCode target) throws IOException;

}
