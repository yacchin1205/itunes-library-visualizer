package net.yzwlab.daap;

/**
 * �A�N�Z�X�R�[�h�̃��|�W�g���ł��B
 */
public interface AccessCodeRepository extends Iterable<AccessCode> {

	/**
	 * �A�N�Z�X�R�[�h���X�V���܂��B
	 * 
	 * @param accessCode
	 *            �A�N�Z�X�R�[�h�Bnull�͕s�B
	 */
	public void updateCode(AccessCode accessCode);

}
