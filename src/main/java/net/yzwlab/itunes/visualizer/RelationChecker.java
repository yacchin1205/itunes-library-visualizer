package net.yzwlab.itunes.visualizer;

/**
 * �֌W�̃`�F�b�N������\������C���^�t�F�[�X�ł��B
 * 
 * @param <T>
 *            �v�f�̃^�C�v�B
 */
public interface RelationChecker<T> {

	/**
	 * �^�C�v���`���܂��B
	 */
	public enum Type {
		NONE, FROM, TO, BOTH
	}

	/**
	 * �֌W���`�F�b�N���܂��B
	 * 
	 * @param elem1
	 *            �v�f1�Bnull�͕s�B
	 * @param elem2
	 *            �v�f2�Bnull�͕s�B
	 * @return �֌W�B
	 */
	public Type checkRelation(T elem1, T elem2);

}
