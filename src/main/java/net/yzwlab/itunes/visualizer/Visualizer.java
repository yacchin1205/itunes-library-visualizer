package net.yzwlab.itunes.visualizer;

import java.io.File;
import java.io.IOException;

import net.yzwlab.daap.AccessCode;
import net.yzwlab.daap.LibraryService;
import net.yzwlab.daap.LibrarySession;
import net.yzwlab.daap.TrackDescription;
import net.yzwlab.daap.util.AbstractCLI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Visualizer for iTunes Library
 */
public class Visualizer extends AbstractCLI {

	/**
	 * Logger
	 */
	private final static Logger logger = LoggerFactory
			.getLogger(Visualizer.class);

	private static final String DEVICE_NAME = "iTunesVisualizer";

	private static final String DEVICE_TYPE = "YZWLAB";

	/**
	 * 実行します。
	 * 
	 * @param args
	 *            引数。nullは不可。
	 * @throws Exception
	 *             エラー。
	 */
	public static void main(String[] args) throws Exception {
		if (args == null) {
			throw new IllegalArgumentException();
		}

		Visualizer viz = new Visualizer(new File("remote.properties"),
				new File(args[0]));
		viz.start();
	}

	/**
	 * 保存先ファイルを保持します。
	 */
	private File targetFile;

	/**
	 * Gephiのビルダを保持します。
	 */
	private GephiBuilder gephiBuilder;

	/**
	 * テストプログラムを構築します。
	 * 
	 * @param propertiesFile
	 *            プロパティ。nullは不可。
	 * @param targetFile
	 *            保存先ファイル。nullは不可。
	 * @throws IOException
	 *             入出力関係のエラー。
	 */
	public Visualizer(File propertiesFile, File targetFile) throws IOException {
		super(propertiesFile, DEVICE_NAME, DEVICE_TYPE);
		if (targetFile == null) {
			throw new IllegalArgumentException();
		}
		this.targetFile = targetFile;
		this.gephiBuilder = new GephiBuilder();
		gephiBuilder.setRelationChecker(new RelationChecker<String>() {
			@Override
			public Type checkRelation(String elem1, String elem2) {
				if (elem1 == null || elem2 == null) {
					throw new IllegalArgumentException();
				}
				elem1 = elem1.trim();
				elem2 = elem2.trim();
				if (elem1.length() <= 2 || elem2.length() <= 2) {
					return Type.NONE;
				}
				if (elem1.equals(elem2)) {
					return Type.BOTH;
				}
				if (elem1.contains(elem2)) {
					return Type.FROM;
				}
				if (elem2.contains(elem1)) {
					return Type.TO;
				}
				return Type.NONE;
			}
		});
	}

	@Override
	public void errorDetected(LibraryService service, Throwable error) {
		if (service == null || error == null) {
			throw new IllegalArgumentException();
		}
		error.printStackTrace();
	}

	@Override
	protected void codeFound(LibraryService service, AccessCode accessCode) {
		if (service == null || accessCode == null) {
			throw new IllegalArgumentException();
		}
		logger.info("アクセステスト: " + accessCode.getAddress() + ":"
				+ accessCode.getPort());
		LibrarySession session = null;
		try {
			session = service.openSession(accessCode);
			for (TrackDescription desc : session.getAllTracks()) {
				gephiBuilder.addSong(desc.getAlbumId(), desc.getArtistId(),
						desc.getName());
			}

			gephiBuilder.output(targetFile);
			System.out.println("done");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				session = null;
			}
		}
		try {
			dispose();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
