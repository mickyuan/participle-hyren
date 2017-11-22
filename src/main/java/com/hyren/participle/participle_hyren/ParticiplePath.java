package com.hyren.participle.participle_hyren;

import java.io.File;
import java.nio.file.Path;

/**
 * 
 * Description: 处理文件的路径对象 
 * 	默认字典位置为：  data/plant.dic（jar包同级目录）
 *
 * Date:2017年11月15日下午5:29:27 
 * Copyright (c) 2017, yuanqi@beyondsoft.com All Rights Reserved.
 * 
 * @author yuanqi 
 * @version  
 * @since JDK 1.7
 */
public class ParticiplePath {

	public static final String DICPATH = "data/plant.dic";
	private Path dicPath = new File(DICPATH).toPath();
	private Path inputFile = null;
	private Path outputFile = null;

	public ParticiplePath(String inputFilePath, String outPutFilePath, String dicPath) throws Exception {

		this(new File(inputFilePath), new File(outPutFilePath), new File(dicPath));
	}

	public ParticiplePath(String inputFilePath, String outPutFilePath) throws Exception {

		this(new File(inputFilePath), new File(outPutFilePath));
	}

	public ParticiplePath(File inputFile, File outputFile, File dicPath) throws Exception {

		this(inputFile, outputFile);
		this.dicPath = dicPath.toPath();
	}

	public ParticiplePath(File inputFile, File outputFile) throws Exception {

		if( null == inputFile || !inputFile.exists() ) {
			throw new Exception("Input file is null or does not exist:" + inputFile);
		}
		if( null == outputFile ) {
			throw new Exception("output file is null");
		}

		this.inputFile = inputFile.toPath();
		this.outputFile = outputFile.toPath();
	}

	/** 
	 * dicPath. 
	 * 
	 * @return  the dicPath 
	 * @since   JDK 1.7
	 */
	public Path getDicPath() {

		return dicPath;
	}

	/** 
	 * dicPath. 
	 * 
	 * @param   dicPath    the dicPath to set 
	 * @since   JDK 1.7 
	 */
	public void setDicPath(Path dicPath) {

		this.dicPath = dicPath;
	}

	/** 
	 * inputFile. 
	 * 
	 * @return  the inputFile 
	 * @since   JDK 1.7
	 */
	public Path getInputFile() {

		return inputFile;
	}

	/** 
	 * inputFile. 
	 * 
	 * @param   inputFile    the inputFile to set 
	 * @since   JDK 1.7 
	 */
	public void setInputFile(Path inputFile) {

		this.inputFile = inputFile;
	}

	/** 
	 * outputFile. 
	 * 
	 * @return  the outputFile 
	 * @since   JDK 1.7
	 */
	public Path getOutputFile() {

		return outputFile;
	}

	/** 
	 * outputFile. 
	 * 
	 * @param   outputFile    the outputFile to set 
	 * @since   JDK 1.7 
	 */
	public void setOutputFile(Path outputFile) {

		this.outputFile = outputFile;
	}

}
