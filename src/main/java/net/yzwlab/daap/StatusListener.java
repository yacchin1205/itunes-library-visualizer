package net.yzwlab.daap;

import org.tunesremote.daap.Status;

/**
 * ��Ԃ̊Ď��p�C���^�t�F�[�X�ł��B
 */
public interface StatusListener {

	/**
	 * ��Ԃ̍X�V�����o���܂��B
	 * 
	 * @param source
	 *            ���o���Bnull�͕s�B
	 * @param updated
	 *            �X�V���ꂽ��ԁBnull�͕s�B
	 */
	public void statusUpdated(Status source, StatusUpdate updated);

}
