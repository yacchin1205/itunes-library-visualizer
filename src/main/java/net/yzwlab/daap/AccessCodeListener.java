package net.yzwlab.daap;

/**
 * ���C�u�����A�N�Z�X�̃��X�i�ł��B
 */
public interface AccessCodeListener {

	/**
	 * �G���[���������ꂽ���Ƃ�ʒm���܂��B
	 * 
	 * @param service
	 *            �T�[�r�X�Bnull�͕s�B
	 * @param error
	 *            �G���[�Bnull�͕s�B
	 */
	public void errorDetected(LibraryService service, Throwable error);

	/**
	 * iTunes�������������Ƃ�ʒm���܂��B
	 * 
	 * @param service
	 *            �T�[�r�X�Bnull�͕s�B
	 * @param accessCode
	 *            �A�N�Z�X�R�[�h�Bnull�͕s�B
	 */
	public void itunesFound(LibraryService service, AccessCode accessCode);

	/**
	 * �A�N�Z�X�R�[�h�������������Ƃ�ʒm���܂��B
	 * 
	 * @param service
	 *            �T�[�r�X�Bnull�͕s�B
	 * @param accessCode
	 *            �A�N�Z�X�R�[�h�Bnull�͕s�B
	 */
	public void accessCodeFound(LibraryService service,
			AccessCode accessCode);

}
