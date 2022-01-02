package com.monitoring.fmonitoringsys.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.monitoring.fmonitoringsys.to.InfoFileTO;
import com.monitoring.fmonitoringsys.to.ResultTO;

/**
 * Il servizio di business che permette di prendere le informazioni dei file da
 * una cartella, stabilita dalla costante "MONITORING_FOLDER_NAME", che si trova
 * sotto a "src/main/resources"
 * 
 * @author amanganiello90
 */
public class FMonitoringService {

	private static final String MONITORING_FOLDER_NAME = "checkFolder";

	/**
	 * Filtra per intervallo data/time l'array di infoFile che si ottiene dal metodo
	 * privato getAllFiles(), ritornando solo i file che rientrano in
	 * quell'intervallo
	 */
	public ResultTO getFilesInfoFromInterval(LocalDateTime startDateTime, LocalDateTime endDateTime) {

		ResultTO result = this.getAllFiles();

		List<InfoFileTO> infoFileList = result.getFiles();
		List<InfoFileTO> filterFileList = new ArrayList<>();

		// se è vuoto (succede nel caso che ci sono stati degli errori), non viene
		// eseguito
		for (InfoFileTO infoFileTO : infoFileList) {
			LocalDateTime lastModify = infoFileTO.getLastModify();
			if (lastModify.isAfter(startDateTime) && lastModify.isBefore(endDateTime)) {
				filterFileList.add(infoFileTO);
			}
		}

		result.setFiles(filterFileList);
		return result;

	}

	/**
	 * Ricerca nella lista dei file ottenuta dal metodo privato getAllFiles(), solo
	 * quello che corrisponde con il checksum MD5 in input
	 */
	public ResultTO getFilesInfoFromMd5(String md5) {

		ResultTO result = this.getAllFiles();

		List<InfoFileTO> infoFileList = result.getFiles();

		boolean isFound = false;

		// se è vuoto (succede nel caso che ci sono stati degli errori), non viene
		// eseguito
		for (InfoFileTO infoFileTO : infoFileList) {
			if (infoFileTO.getHashMd5().equals(md5)) {
				isFound = true;
				result.setFiles(Arrays.asList(infoFileTO));
				break;
			}

		}

		// nel caso in cui ci sono stati errori precedenti, non mi deve ritornare il
		// seguente risultato. Questo messaggio è se c'erano file e nessuno ha
		// corrispondenza con l' Md5
		if (!isFound && !infoFileList.isEmpty()) {
			return new ResultTO("Non è stato trovato nessun file corrispondente al tuo checksum MD5",
					new ArrayList<InfoFileTO>());
		}

		return result;

	}

	private ResultTO getAllFiles() {

		URL folderUrl = this.getFolderUrl();
		if (folderUrl == null) {
			return new ResultTO("La cartella " + MONITORING_FOLDER_NAME + " da monitorare non esiste oppure è vuota!",
					new ArrayList<InfoFileTO>());
		}

		String folderPath = folderUrl.getPath();

		ResultTO result = this.getFilesInfoFromFolder(folderPath);

		return result;

	}

	private ResultTO getFilesInfoFromFolder(String folderPath) {
		ResultTO result;
		String fileName = null;
		List<InfoFileTO> infoFileList = new ArrayList<>();

		File folderFile = new File(folderPath);

		try {

			for (final File fileEntry : folderFile.listFiles()) {
				InfoFileTO infoFile = new InfoFileTO();
				if (fileEntry.isDirectory()) {
					// non considero sottocartelle con i file, quindi non leggo ricorsivamenente il
					// contenuto
				} else {
					fileName = fileEntry.getName();
					infoFile.setName(fileName);
					infoFile.setPath(fileEntry.getAbsolutePath());

					Path infoFilePath = Paths.get(infoFile.getPath());
					long byteSize = Files.size(infoFilePath);
					infoFile.setByteSize(byteSize);

					long epoch = fileEntry.lastModified();
					// conversione epoch in localdateTime per avere l'ultima modifica. Il formato
					// sarà "yyyy-mm-ddThh:mm:ss"
					LocalDateTime lastModify = Instant.ofEpochMilli(epoch).atZone(ZoneId.systemDefault())
							.toLocalDateTime();
					infoFile.setLastModify(lastModify);

					// calcolo MD5 checksum con il codec di Apache
					InputStream is = Files.newInputStream(infoFilePath);
					infoFile.setHashMd5(DigestUtils.md5Hex(is));

					infoFileList.add(infoFile);

				}
			}

			result = new ResultTO("Operazione conclusa con successo", infoFileList);

		} catch (IOException e) {
			result = new ResultTO("Errore nella lettura fisica del file " + fileName + " per recuperare la size",
					new ArrayList<InfoFileTO>());

		}

		return result;

	}

	private URL getFolderUrl() {

		ClassLoader classLoader = getClass().getClassLoader();

		return classLoader.getResource(MONITORING_FOLDER_NAME);

	}

}
