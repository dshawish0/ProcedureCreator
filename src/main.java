import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class main {
    public static void main(String args[]) throws FileNotFoundException, IOException, Exception {
        Scanner x = new Scanner(System.in);
        List<String> attributes = new ArrayList<>();
        List<String> dataType = new ArrayList<>();
        System.out.println("Enter Table Name");
        String TableName=x.next();

        for (int i = 0; ; i++) {
            if(i!=0){
                System.out.println("Enter 0 to stop or 1 to continue");
                int k = x.nextInt();
                if(k==0)
                    break;
            }
                System.out.println("Enter attributes name");
                attributes.add(x.next());
                System.out.println("Enter dataType for attributes");
                dataType.add(x.next());

        }

        System.out.println(PackageCreate(attributes,dataType,TableName));
        System.out.println(PackageBodyCreate(attributes,dataType,TableName));





    }

    public static String PackageCreate(List<String> attributes, List<String> dataType,String TableName){
        String Package="CREATE OR REPLACE PACKAGE "+TableName+"CRUD_Package AS\n";
        Package += "Procedure "+TableName+"CRUD(\n";
        Package+="crud in varchar,\n";
        for (int i = 0; i <attributes.size(); i++) {
            if(attributes.size()-1==i)
                Package += attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+" in " +dataType.get(i)+" default null);\n";
            else
                Package += attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+" in " +dataType.get(i)+" default null,\n";
        }

        Package += "procedure getById("+attributes.get(0).toUpperCase().charAt(0)+attributes.get(0)+" in "+dataType.get(0)+");\n";

        Package += "end "+TableName+"CRUD_Package;" ;



        return Package;

    }

    public static String PackageBodyCreate(List<String> attributes, List<String> dataType,String TableName){

        System.out.println("\n\n\n\n");
        String Package="CREATE OR REPLACE PACKAGE Body "+TableName+"CRUD_Package AS\n";
        Package += "Procedure "+TableName+"CRUD(\n";
        Package+="crud in varchar,\n";
        for (int i = 0; i <attributes.size(); i++) {
            if(attributes.size()-1==i)
                Package += attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+" in " +dataType.get(i)+" default null)\n\n\n";
            else
                Package += attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+" in " +dataType.get(i)+" default null,\n";
        }
        Package += "as \n" +
                "c_all sys_refcursor;\n" +
                "\n" +
                "begin \n";


        Package +="if crud ='C' then \n";
        Package+="      INSERT INTO "+TableName+"(";
        for (int i = 1; i <attributes.size(); i++) {
            if(attributes.size()-1==i)
                Package += attributes.get(i)+")\n";
            else
                Package += attributes.get(i)+", ";
        }

        Package+="      VALUES(";

        for (int i = 1; i <attributes.size(); i++) {
            if(attributes.size()-1==i)
                Package +=attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+");\n";
            else
                Package += attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+", ";
        }

        Package+="      COMMIT;\n\n\n";


        Package+= "elsif crud='U' then\n";
        Package+= "     UPDATE "+ TableName +" SET ";

        for (int i = 1; i <attributes.size(); i++) {
            if(attributes.size()-1==i)
                Package += attributes.get(i)+ "=" +attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+
                        " WHERE " +attributes.get(0)+"="+attributes.get(0).toUpperCase().charAt(0)+attributes.get(0)+";\n";
            else
                Package += attributes.get(i)+ "=" +attributes.get(i).toUpperCase().charAt(0)+attributes.get(i)+", ";
        }
        Package+="      COMMIT;\n\n\n";


        Package+="elsif crud = 'D' then \n";
        Package+="      DELETE From " +TableName+" WHERE "+attributes.get(0)+ "=" +attributes.get(0).toUpperCase().charAt(0)+attributes.get(0)+";\n";
        Package+="      COMMIT;\n\n\n";


        Package+="elsif crud = 'G'  then \n";
        Package += "    open c_all for \n";
        Package += "    select * from "+TableName+";\n";
        Package+="      DBMS_SQL.RETURN_RESULT(c_all);\n";

        Package+="end if;\n\n\n";

        Package += "end "+TableName+"CRUD;\n";


        Package += "procedure getById("+attributes.get(0).toUpperCase().charAt(0)+attributes.get(0)+
                " in "+dataType.get(0)+")\n"+
                "as\n"+
                "c_all sys_refcursor;\n"+
                "begin\n"+
                "open c_all for\n"+"select * from "+TableName+" where "+attributes.get(0)+ "=" +attributes.get(0).toUpperCase().charAt(0)+attributes.get(0)+";\n"+
                "dbms_sql.return_result(c_all);\n"+"end getById;\n";

        Package += "end "+TableName+"CRUD_Package;" ;

        return Package;

    }


}
