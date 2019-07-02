package com.hello.service.impl;

import com.hello.model.Trie;
import com.hello.model.WordEntry;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 *  clone from https://github.com/NLPchina/Word2VEC_java.git
 *
 *  date 2019/4/27
 */

public class Word2VEC {

	private HashMap<String, float[]> wordMap = new HashMap<String, float[]>();
	public Trie trieTree = new Trie();

	private int words;
	private int size;
	private int topNSize = 40;

	public int getMapSize(){
		return wordMap.size();
	}


	public Word2VEC(String modelPath){
		/*
		try {
			loadTxtModel(modelPath);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}*/
	}

	public String[] findWordsByPrefix(String prefix){
		return trieTree.findWordsByPrefix(prefix);
	}


	public void loadTxtModel(String path) throws IOException{
		// TODO add frequency of word into model
		System.out.println("-------- loading txt lexcion --------");
		try{
			FileInputStream fis = new FileInputStream(path);
			InputStreamReader isr = new InputStreamReader(fis, "UTF-8");
			BufferedReader br = new BufferedReader(isr);//构造一个BufferedReader类来读取文件
			String paraString = br.readLine();
			String[] paraSubString = paraString.split(" ");

			int words = Integer.parseInt(paraSubString[0]);
			int dimension = Integer.parseInt(paraSubString[1]);
			System.out.printf("### total number of words in lexcion is : %d \n### the dimension of word vector is : %d \n",words,dimension);
			float[] vectors = null;
			double len = 0;
			for (int i = 0; i < words; i++) {
				String item = br.readLine();
				String[] subItem = item.split(" ");
				String word = subItem[0];
				if(i%10000==0) {
					System.out.printf("## loading txt lexcion, current loaded words number : %d\n", i);
					System.out.println("the loaded word is " + word);
				}
				vectors = new float[dimension];
				for (int j = 0; j < dimension; j++) {
					float vector = Float.parseFloat(subItem[j+1]);
					len += vector * vector;
					vectors[j] = (float) vector;
				}
				len = Math.sqrt(len);
				for (int j = 0; j < dimension; j++) {
					vectors[j] /= len;
				}
				wordMap.put(word, vectors);
				trieTree.insert(word);
			}
			br.close();
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * 加载模型,binary
	 * 
	 * @param path
	 *            模型的路径
	 * @throws IOException
	 */
	public void loadGoogleModel(String path) throws IOException {
		DataInputStream dis = null;
		BufferedInputStream bis = null;
		double len = 0;
		float vector = 0;
		try {
			bis = new BufferedInputStream(new FileInputStream(path));
			dis = new DataInputStream(bis);
			// //读取词数
			words = Integer.parseInt(readString(dis));
			// //大小
			size = Integer.parseInt(readString(dis));
			String word;
			float[] vectors = null;
			for (int i = 0; i < words; i++) {
				String inword = readString(dis);
				word = new String(inword.getBytes("gbk"),"utf-8");
				vectors = new float[size];
				len = 0;
				for (int j = 0; j < size; j++) {
					vector = readFloat(dis);
					len += vector * vector;
					vectors[j] = (float) vector;
				}
				len = Math.sqrt(len);

				for (int j = 0; j < size; j++) {
					vectors[j] /= len;
				}

				wordMap.put(word, vectors);
				dis.read();
			}
		} finally {
			bis.close();
			dis.close();
		}
	}

	/**
	 * 加载模型
	 * 
	 * @param path
	 *            模型的路径closestWordSet
	 * @throws IOException
	 */
	public void loadJavaModel(String path) throws IOException {
		try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(path)))) {
			words = dis.readInt();
			size = dis.readInt();

			float vector = 0;

			String key = null;
			float[] value = null;
			for (int i = 0; i < words; i++) {
				double len = 0;
				key = dis.readUTF();
				value = new float[size];
				for (int j = 0; j < size; j++) {
					vector = dis.readFloat();
					len += vector * vector;
					value[j] = vector;
				}

				len = Math.sqrt(len);

				for (int j = 0; j < size; j++) {
					value[j] /= len;
				}
				wordMap.put(key, value);
			}

		}
	}

	private static final int MAX_SIZE = 50;

	/**
	 * analogy relation of word2 and word3 and find the corresponding word for word1.
	 *
	 * exmple : queen - women + man = king
	 * 
	 * @return
	 */
	public TreeSet<WordEntry> analogy(String word0, String word1, String word2) {
		float[] wv0 = getWordVector(word0);
		float[] wv1 = getWordVector(word1);
		float[] wv2 = getWordVector(word2);

		if (wv1 == null || wv2 == null || wv0 == null) {
			return null;
		}
		float[] wordVector = new float[size];
		for (int i = 0; i < size; i++) {
			wordVector[i] = wv1[i] - wv0[i] + wv2[i];
		}
		float[] tempVector;
		String name;
		List<WordEntry> wordEntrys = new ArrayList<WordEntry>(topNSize);
		for (Entry<String, float[]> entry : wordMap.entrySet()) {
			name = entry.getKey();
			if (name.equals(word0) || name.equals(word1) || name.equals(word2)) {
				continue;
			}
			float dist = 0;
			tempVector = entry.getValue();
			for (int i = 0; i < wordVector.length; i++) {
				dist += wordVector[i] * tempVector[i];
			}
			insertTopN(name, dist, wordEntrys);
		}
		return new TreeSet<WordEntry>(wordEntrys);
	}
	public boolean has(String queryWord){
		return trieTree.has(queryWord);
	}

	private void insertTopN(String name, float score, List<WordEntry> wordsEntrys) {
		if (wordsEntrys.size() < topNSize) {
			wordsEntrys.add(new WordEntry(name, score));
			return;
		}
		float min = Float.MAX_VALUE;
		int minOffe = 0;
		for (int i = 0; i < topNSize; i++) {
			WordEntry wordEntry = wordsEntrys.get(i);
			if (min > wordEntry.getScore()) {
				min = wordEntry.getScore();
				minOffe = i;
			}
		}
		if (score > min) {
			wordsEntrys.set(minOffe, new WordEntry(name, score));
		}

	}

	public Set<WordEntry> distance(String queryWord) {

		float[] center = wordMap.get(queryWord);
		if (center == null) {
			return Collections.emptySet();
		}

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double min = Float.MIN_VALUE;
		scan(center, resultSize, result, min);
		result.pollFirst();

		return result;
	}

	private void scan(float[] center, int resultSize, TreeSet<WordEntry> result, double min) {
		for (Entry<String, float[]> entry : wordMap.entrySet()) {
			float[] vector = entry.getValue();
			float dist = 0;
			for (int i = 0; i < vector.length; i++) {
				dist += center[i] * vector[i];
			}

			if (dist > min) {
				result.add(new WordEntry(entry.getKey(), dist));
				if (resultSize < result.size()) {
					result.pollLast();
				}
				min = result.last().getScore();
			}
		}
	}


	/** using the average word vector of a set of words as query vector */
	public Set<WordEntry> distance(List<String> words) {

		float[] center = null;
		for (String word : words) {
			center = sum(center, wordMap.get(word));
		}

		if (center == null) {
			return Collections.emptySet();
		}

		int resultSize = wordMap.size() < topNSize ? wordMap.size() : topNSize;
		TreeSet<WordEntry> result = new TreeSet<WordEntry>();

		double min = Float.MIN_VALUE;
		scan(center, resultSize, result, min);
		result.pollFirst();
		return result;
	}

	public float compare2(String word1, String word2){
		float[] wv1 = wordMap.get(word1);
		float[] wv2 = wordMap.get(word2);
		float dist = 0;
		for (int i = 0; i < wv1.length; i++) {
			dist += wv2[i] * wv1[i];
		}
		return dist;
	}

	private float[] sum(float[] center, float[] fs) {

		if (center == null && fs == null) {
			return null;
		}

		if (fs == null) {
			return center;
		}

		if (center == null) {
			return fs;
		}

		for (int i = 0; i < fs.length; i++) {
			center[i] += fs[i];
		}

		return center;
	}

	/**
	 * 得到词向量
	 * 
	 * @param word
	 * @return
	 */
	public float[] getWordVector(String word) {
		return wordMap.get(word);
	}

	public static float readFloat(InputStream is) throws IOException {
		byte[] bytes = new byte[4];
		is.read(bytes);
		return getFloat(bytes);
	}

	/**
	 * 读取一个float
	 * 
	 * @param b
	 * @return
	 */
	public static float getFloat(byte[] b) {
		int accum = 0;
		accum = accum | (b[0] & 0xff) << 0;
		accum = accum | (b[1] & 0xff) << 8;
		accum = accum | (b[2] & 0xff) << 16;
		accum = accum | (b[3] & 0xff) << 24;
		return Float.intBitsToFloat(accum);
	}

	/**
	 * 读取一个字符串
	 * 
	 * @param dis
	 * @return
	 * @throws IOException
	 */
	private static String readString(DataInputStream dis) throws IOException {
		byte[] bytes = new byte[MAX_SIZE];
		byte b = dis.readByte();
		int i = -1;
		StringBuilder sb = new StringBuilder();
		while (b != 32 && b != 10) {
			i++;
			bytes[i] = b;
			b = dis.readByte();
			if (i == 49) {
				sb.append(new String(bytes));
				i = -1;
				bytes = new byte[MAX_SIZE];
			}
		}
		sb.append(new String(bytes, 0, i + 1));
		return sb.toString();
	}

	public int getTopNSize() {
		return topNSize;
	}

	public void setTopNSize(int topNSize) {
		this.topNSize = topNSize;
	}

	public HashMap<String, float[]> getWordMap() {
		return wordMap;
	}

	public int getWords() {
		return words;
	}

	public int getSize() {
		return size;
	}

}
