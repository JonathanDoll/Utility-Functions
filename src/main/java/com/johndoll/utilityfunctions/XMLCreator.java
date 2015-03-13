package com.johndoll.utilityfunctions;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Jonathan Doll
 */
public class XMLCreator {

    private Stack<String> tags = new Stack();
    private FileOutputStream fout;
    private ArrayList<String>[] storedTags = new ArrayList[10];

    public XMLCreator(String filePath, String fileName) {
        try {
            fout = new FileOutputStream(filePath + "\\" + fileName + ".xml");
        } catch (FileNotFoundException e) {
            System.err.println(e);
        }
    }

    public void addTag(String tag) {
        try {
            tags.push(tag);
            fout.write(("<" + tag + ">\n").getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void addTag(String tag, String attributes) {
        try {
            tags.push(tag);
            fout.write(("<" + tag + " " + attributes + ">\n").getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void closeTag(String tag) {
        if (tags.contains(tag)) {
            try {
                while (!tag.equals(tags.peek())) {
                    fout.write(("</" + tags.pop() + ">\n").getBytes());
                }
                fout.write(("</" + tags.pop() + ">\n").getBytes());
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public void addData(String data) {
        try {
            fout.write(data.getBytes());
            fout.write("\n".getBytes());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public String getCurrentTag() {
        return tags.peek();
    }

    public void closeRemainingTags() {
        try {
            while (!tags.empty()) {
                fout.write(("</" + tags.pop() + ">\n").getBytes());
            }
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void closeFile() {
        try {
            fout.close();
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    public void addDataToStoredTag(String outerTag, String innerTag, String data) {
        int i = 0;
        boolean tagFound = false;
        while (!tagFound || i < storedTags.length) {
            if (storedTags[i] != null) {
                if (storedTags[i].get(0).equals(outerTag)) {
                    tagFound = true;
                    storedTags[i].add("<" + innerTag + ">");
                    storedTags[i].add(data);
                    storedTags[i].add("</" + innerTag + ">");
                }
            } else {
                break;
            }
            i++;
        }

        if (!tagFound) {
            System.err.println("Tag " + outerTag + " was not found");
        }
    }

    public void storeTag(String tag) {
        int i = 0;
        boolean emptySlot = false;

        while (!emptySlot && i < storedTags.length) {
            if (storedTags[i] == null) {
                storedTags[i] = new ArrayList();
                emptySlot = true;
                storedTags[i].add(tag);
            }
            i++;
        }

        if (!emptySlot) {
            System.err.println("Tag storer is full");
        }

    }

    public void submitStoredTags() {
        int i = 0;
        while (storedTags[i] != null && i < storedTags.length) {
            addTag("" + storedTags[i].get(0));
            for (int j = 1; j < storedTags[i].size(); j++) {
                addData("" + storedTags[i].get(j));
            }
            closeTag(getCurrentTag());
            i++;
        }
        storedTags = new ArrayList[10];
    }

    
}
