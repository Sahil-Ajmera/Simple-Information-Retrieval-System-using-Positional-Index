//package lab2;

import java.util.ArrayList;
import java.io.*;
import java.util.*

//Supporting Class
/**
 *
 * @author Sahil Ajmera
 * Document id class that contains the document id and the position list
 */
class DocId{
    int docId;
    ArrayList<Integer> positionList;
    public DocId(int did)
    {
        docId = did;
        positionList = new ArrayList<Integer>();
    }
    public DocId(int did, int position)
    {
        docId = did;
        positionList = new ArrayList<Integer>();
        positionList.add(new Integer(position));
    }

    public void insertPosition(int position)
    {
        positionList.add(new Integer(position));
    }

    public String toString()
    {
        String docIdString = ""+docId + ":<";
        for(Integer pos:positionList)
            docIdString += pos + ",";
        docIdString = docIdString.substring(0,docIdString.length()-1) + ">";
        return docIdString;
    }
