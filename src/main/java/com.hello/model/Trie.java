package com.hello.model;

import java.util.ArrayList;
import java.util.HashMap;


/**
 *  author https://www.cnblogs.com/xujian2014/p/5614724.html
 *
 *
 */
public class Trie
{
    private TrieNode root;// 字典树的根

    class TrieNode // 字典树节点
    {
        private HashMap<String,TrieNode> son;// 所有的儿子节点
        private boolean isEnd;// 是不是最后一个节点
        private char val;// 节点的值
        //private String string;

        @Override
        public String toString() {
            return "TrieNode{" +
                    "son=" + son +
                    ", isEnd=" + isEnd +
                    ", val=" + val +
                    //", string='" + string + '\'' +
                    '}';
        }

        TrieNode()
        {
            son = new HashMap<>();
            isEnd = false;

        }
    }
    public Trie() // 初始化字典树
    {
        root = new TrieNode();
    }


    // 建立字典树
    public void insert(String str) // 在字典树中插入一个单词
    {
        //System.out.println("insert : "+str );
        if (str == null || str.length() == 0)
        {
            System.out.println("empty string");
        }
        TrieNode node = root;
        char[] letters = str.toCharArray();//将目标单词转换为字符数组
        for (int i = 0, len = str.length(); i < len; i++)
        {
            if(node.son.containsKey(""+letters[i])){
                node=node.son.get(""+letters[i]);
            }else{
                TrieNode newNode = new TrieNode();
                newNode.val = letters[i];
                /*String s = "";
                for(int j=0;j<=i;j++){
                    s += letters[j];
                }
                newNode.string=s;*/
                node.son.put(""+letters[i],newNode);
                node = node.son.get(""+letters[i]);
            }
        }
        node.isEnd = true;
        //printAll(true,null);
    }

    public String[] findWordsByPrefix(String prefix){
        TrieNode node = findNodeByPrefix(prefix);
        //System.out.println(node);
        if(node == null)
            return null;
        ArrayList<String>result = new ArrayList();
        preTraverse(result,node,prefix);
        String[] resultArray = new String[result.size()];
        for(int i=0;i<result.size();i++){
            resultArray[i] = result.get(i);
        }
        return resultArray;
    }

    // 根据字符串找节点
    private TrieNode findNodeByPrefix(String prefix)
    {
        if (prefix == null || prefix.length() == 0)
        {
            return null;
        }
        TrieNode node = root;
        char[] letters = prefix.toCharArray();
        for (int i = 0, len = prefix.length(); i < len; i++)
        {
            if (node.son.containsKey(""+letters[i]))
            {
                node = node.son.get(""+letters[i]);
            }
            else
            {
                return null;
            }
        }
        return node;
    }

    // 遍历经过此节点的单词.
    private void preTraverse(ArrayList<String>result,TrieNode node, String prefix)
    {
        if(node.isEnd)
            result.add(prefix);
        if (!node.son.isEmpty())
        {

            for (TrieNode child : node.son.values())
            {
                if (child != null)
                {
                    preTraverse(result,child, prefix + child.val);
                }
            }
        }
    }

/*
    public void printAll(boolean isRoot,TrieNode node){
        if(isRoot){
            node = this.root;
        }
        for(TrieNode child : node.son.values()){
            //System.out.println("this is "+node.string+", child is "+child.string);

            if(child.isEnd)
                System.out.println(child.string);
            printAll(false,child);
        }
    }
*/
    // 在字典树中查找一个完全匹配的单词.
    public boolean has(String str)
    {
        if(str==null||str.length()==0)
        {
            return false;
        }
        TrieNode node=root;
        char[]letters=str.toCharArray();
        for(int i=0,len=str.length(); i<len; i++)
        {
            if(node.son.containsKey(""+letters[i]))
            {
                node=node.son.get(""+letters[i]);
            }
            else
            {
                return false;
            }
        }
        //走到这一步，表明可能完全匹配，可能部分匹配，如果最后一个字符节点为末端节点，则是完全匹配，否则是部分匹配
        return node.isEnd;
    }


    public TrieNode getRoot()
    {
        return this.root;
    }

}