package net.yzwlab.daap;

/**
 * �g���b�N�̏����`���܂��B
 */
public interface TrackDescription {

	/**
	 * ���O���擾���܂��B
	 * 
	 * @return ���O�B
	 */
	public String getName();

	/**
	 * �A�[�e�B�X�gID���擾���܂��B
	 * 
	 * @return �A�[�e�B�X�gID�B
	 */
	public String getArtistId();

	/**
	 * �A���o��ID���擾���܂��B
	 * 
	 * @return �A���o��ID�B
	 */
	public String getAlbumId();

}
