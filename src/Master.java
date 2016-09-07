/**
 * Created by saian on 19.08.16.
 */
public class Master {

    private String name = "";
    private String surname="";
    private String login ="";
    private String telnumb="";
    private String comandMaster="";


public Master (String name,String surname,String login, String telnumb, String comandMaster){
    this.name = name;
    this.surname = surname;
    this.login = login;
    this.telnumb = telnumb;
    this.comandMaster = comandMaster;
}

public String getName (){
    return this.name;
}
public String getComandMaster(){
    return this.comandMaster;
}
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (o == null)
            return false;
        if (!(getClass() == o.getClass()))
            return false;
        else {
            Master rec = (Master) o;
            if ((rec.login.equals(this.login)) && (rec.comandMaster.equals(this.comandMaster)))
                return true;
            else
                return false;
        }
    }

    @Override
    public int hashCode(){
        return this.comandMaster.hashCode()+this.login.hashCode();

    }
}
