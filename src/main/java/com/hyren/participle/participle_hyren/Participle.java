package com.hyren.participle.participle_hyren;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

/**
 * 
 * Description: 处理文本文件：
 * 	1.字典出现的词，但是并未在文本文件中标注的，标注出来
 * 	2.字典未出现的词，但是 在文本文件中标注了，就将这个词写入到字典 
 *
 * Date:2017年11月15日下午5:37:33 
 * Copyright (c) 2017, yuanqi@beyondsoft.com All Rights Reserved.
 * 
 * @author yuanqi 
 * @version  
 * @since JDK 1.7
 */
public class Participle {

	public static final String LINEDELIMITER = System.getProperties().getProperty("line.separator");
	public static final String PLANTPREFFIX = "plant";
	public static final Charset charset = Charset.forName("UTF-8");

	ParticiplePath participlePath = null;

	public Participle(ParticiplePath participlePath) {

		this.participlePath = participlePath;
	}

	public Participle() {}

	/** 
	 * participlePath. 
	 * 
	 * @return  the participlePath 
	 * @since   JDK 1.7
	 */
	public ParticiplePath getParticiplePath() {

		return participlePath;
	}

	/** 
	 * participlePath. 
	 * 
	 * @param   participlePath    the participlePath to set 
	 * @since   JDK 1.7 
	 */
	public void setParticiplePath(ParticiplePath participlePath) {

		this.participlePath = participlePath;
	}

	/*
	 * 从字典中获取分词，分词字典中每行为一个单词，不可有空行，
	 */
	private List<String> getParticipleList() {

		try {
			return Files.readAllLines(participlePath.getDicPath(), charset);
		}
		catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 用于可以调用的方法
	 */
	public void participleEnvent() throws Exception {

		if( null == this.participlePath ) {
				throw new Exception("ParticiplePath must not null!");
		}
		/*
		 * 为啥要执行两次呢？
		 * 因为人去标注的时候，有可能重复的词，后面标注的前面没有标注 ，导致前面的没有这些词没有处理，所以要执行两次使所有的重复的词都被处理
		 */
		proccessFile();
		proccessFile();
	}

	/**
	 * 根据字典和标注文本对字典和文本做处理
	 *    
	 * @author yuanqi
	 * Date:2017年11月15日下午6:05:47 
	 * @since JDK 1.7
	 */
	private void proccessFile() {

		//字典中的分词
		List<String> participleList = getParticipleList();
		//存储已经对文本处理过的词。（对于一行，某个分词修改过文本之后，这个分词第二次遇到，就不会再对文本做处理）
		List<String> alreadyReplaced = new ArrayList<>();
		//输出内容
		List<String> outputContent = new ArrayList<>();

		try (BufferedWriter bw = Files.newBufferedWriter(participlePath.getDicPath(), charset, StandardOpenOption.APPEND)) {

			List<String> list = Files.readAllLines(participlePath.getInputFile(), charset);//输入文件的内容存入到list中
			for(int i = 0, k = list.size(); i < k; i++) {//循环每一行
				alreadyReplaced.clear();

				String line = list.get(i);
				if( StringUtils.isBlank(line) ) {//如果这一行是空行，就直接原样输出
					outputContent.add(line);
					continue;
				}

				if( line.startsWith(PLANTPREFFIX) ) {//说明标注为植物了

					String word = line.replaceFirst(PLANTPREFFIX, "");//这个词就是标注为植物的词
					if( !participleList.contains(word) ) {//标注为植物了，但是词库中没有
						participleList.add(word);//这个词放到list中以免下面遇到相同的词又重复处理一次
						bw.newLine();
						bw.write(word);//写到词库文件中
					}
				}
				else {
					List<Term> segment = HanLP.segment(line);//把整个一行由分词库分词
					for(int j = 0; j < segment.size(); j++) {//遍历每一个分出来的词
						String participleWord = segment.get(j).word;
						if( StringUtils.isBlank(participleWord) ) {//分词为空不处理
							continue;
						}
						//文本未标注，字典中存在的词，就对文本做处理（如果这个词已经对文本做过处理，就不二次处理）
						if( participleList.contains(participleWord) && !alreadyReplaced.contains(participleWord) ) {
							alreadyReplaced.add(participleWord);
							//文本处理
							line = line.replaceAll(participleWord, LINEDELIMITER + PLANTPREFFIX + participleWord + LINEDELIMITER);
						}
					}
				}
				outputContent.add(line);
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		writeListToFile(outputContent);//将输出内容写到输出文本文件中
	}

	/*
	 * 把list的内容写到输出文本中
	 */
	private void writeListToFile(List<String> outputContent) {

		try (BufferedWriter bw = Files.newBufferedWriter(participlePath.getOutputFile(), charset)) {
			for(int i = 0, k = outputContent.size(); i < k; i++) {
				try {
					bw.write(outputContent.get(i));
					bw.newLine();
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		String inputFilePath = "e:\\plant.txt";
		String outputFilePath = "e:\\pplant.txt";

		try {
			Participle participle = new Participle();
			participle.setParticiplePath(new ParticiplePath(inputFilePath, outputFilePath));
			participle.participleEnvent();
		}
		catch(Exception e) {
			e.printStackTrace();
		}

	}

}
