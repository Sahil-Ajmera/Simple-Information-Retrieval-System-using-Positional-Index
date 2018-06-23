//package lab2;

import java.util.ArrayList;
import java.io.*;
import java.util.*


public class PositionalIndex {
    String[] myDocs;
    ArrayList<String> termList;
    ArrayList<ArrayList<DocId>> docLists;

    /**
     * Construct a positional index
     * @param docs List of input strings or file names
     *
     */
    public PositionalIndex(String[] docs)
    {
        //TASK1: TO BE COMPLETED
        System.out.println("****************************************Task-1****************************************");
        // Taking all string into one.

        termList = new ArrayList<String>();
        docLists = new ArrayList<ArrayList<DocId>>();

        try
        {

            for(int i = 0 ; i< docs.length;i++)
            {
                String all_lines = "";
                File docFile = new File("Lab1_Data" + '/' + docs[i]);
                BufferedReader reader = new BufferedReader(new FileReader(docFile));
                String line = null;
                while((line = reader.readLine()) != null)
                {

                    // Normalization - Take all lower case  .
                    all_lines +=  line.toLowerCase();
                }

                // Tokenization
                String[] tokens  = all_lines.split("[ .,?!:;$%&*+()%#!/\\-\\^\"]+");

                for(int j =0 ; j<tokens.length;j++)
                {
                    if(!termList.contains(tokens[j]))
                    {
                        termList.add(tokens[j]);
                        DocId doid = new DocId(i,j);
                        ArrayList<DocId> docList = new ArrayList<DocId>();
                        docList.add(doid);
                        docLists.add(docList);
                    }
                    else
                    {
                        int index = termList.indexOf(tokens[j]);
                        ArrayList<DocId> docList = docLists.get(index);
                        boolean match = false;
                        int k = 0;

                        //old term same document also seen before
                        for(DocId doid:docList)
                        {
                            if(doid.docId == i)
                            {
                                doid.insertPosition(j);
                                match = true;
                                //docList.set(k,doid);
                            }
                            k++;
                        }

                        //old term new document
                        if(!match)
                        {
                            DocId doid = new DocId(i,j);
                            docLists.get(index).add(doid);
                        }
                    }
                }
            }
        }
        catch (Exception ex)
        {
            for(int i = 0 ;i<docs.length;i++)
            {
                String all_lines = "";
                all_lines += docs[i].toLowerCase();

                // Tokenization
                String[] tokens  = all_lines.split("[ .,?!:;$%&*+()%#!/\\-\\^\"]+");

                for(int j =0 ; j<tokens.length;j++)
                {
                    if(!termList.contains(tokens[j]))
                    {
                        termList.add(tokens[j]);
                        DocId doid = new DocId(i,j);
                        ArrayList<DocId> docList = new ArrayList<DocId>();
                        docList.add(doid);
                        docLists.add(docList);
                    }
                    else
                    {
                        int index = termList.indexOf(tokens[j]);
                        ArrayList<DocId> docList = docLists.get(index);
                        boolean match = false;
                        int k = 0;

                        //old term same document also seen before
                        for(DocId doid:docList)
                        {
                            if(doid.docId == i)
                            {
                                doid.insertPosition(j);
                                match = true;
                                //docList.set(k,doid);
                            }
                            k++;
                        }

                        //old term new document
                        if(!match)
                        {
                            DocId doid = new DocId(i,j);
                            docLists.get(index).add(doid);
                        }
                    }
                }
            }
        }

    }

    /**
     * Return the string representation of a positional index
     */
    public String toString()
    {
        String matrixString = new String();
        ArrayList<DocId> docList;
        for(int i=0;i<termList.size();i++){
            matrixString += String.format("%-15s", termList.get(i));
            docList = docLists.get(i);
            for(int j=0;j<docList.size();j++)
            {
                matrixString += docList.get(j)+ "\t";
            }
            matrixString += "\n";
        }
        return matrixString;
    }



   public static void main(String[] args)
    {
        String[] docs = {"new home sales top forecasts",
                "home sales rise in july",
                "increase in home sales in july",
                "july new home sales rise"
        };
        Scanner scanner = new Scanner(System.in);
        //place files in Lab1_Data folder if using files
        //String[] docs = {"cv000_29416.txt","cv001_19502.txt","cv002_17424.txt","cv003_12683.txt","cv004_12641.txt"};
        PositionalIndex pi = new PositionalIndex(docs);
        System.out.print(pi);
    }

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
