package net.yzwlab.daap.impl;

import org.tunesremote.daap.Paired;
import org.tunesremote.daap.PairingServer;

/**
 * Android��Handler�̑�p�C���^�[�t�F�C�X�B
 */
public interface PairingListener {

	/**
	 * �y�A�����O���ꂽ���Ƃ�ʒm���܂��B
	 * 
	 * @param source
	 *            �������Bnull�͕s�B
	 * @param paired
	 *            �y�A�����O���ꂽ���Bnull�͕s�B
	 */
	public void paired(PairingServer source, Paired paired);

}
