package tech.caols.infinitely.db;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "test")
public class Test {
    @Id
    @Column(name = "idtest")
    private int idtest;

    @Column(name = "tinyint", nullable = false)
    private byte tinyint;

    @Column(name = "smallint", nullable = false)
    private short smallint;

    @Column(name = "mediumint", nullable = false)
    private int mediumint;

    @Column(name = "bigint", nullable = false)
    private long bigint;

    @Column(name = "float", nullable = false, precision = 2)
    private float aFloat;

    @Column(name = "double", nullable = false, precision = 2)
    private double aDouble;

    @Column(name = "decimal", nullable = false, precision = 2)
    private BigDecimal decimal;

    @Column(name = "datetime", nullable = false)
    private Date datetime;

    @Column(name = "string", nullable = false, length = 45)
    private String string;

    @Column(name = "text", nullable = false, columnDefinition = "text")
    private Reader text;

    @Column(name = "blob", nullable = false, columnDefinition = "blob")
    private InputStream blob;

    public int getIdtest() {
        return idtest;
    }

    public void setIdtest(int idtest) {
        this.idtest = idtest;
    }

    public byte getTinyint() {
        return tinyint;
    }

    public void setTinyint(byte tinyint) {
        this.tinyint = tinyint;
    }

    public short getSmallint() {
        return smallint;
    }

    public void setSmallint(short smallint) {
        this.smallint = smallint;
    }

    public int getMediumint() {
        return mediumint;
    }

    public void setMediumint(int mediumint) {
        this.mediumint = mediumint;
    }

    public long getBigint() {
        return bigint;
    }

    public void setBigint(long bigint) {
        this.bigint = bigint;
    }

    public float getaFloat() {
        return aFloat;
    }

    public void setaFloat(float aFloat) {
        this.aFloat = aFloat;
    }

    public double getaDouble() {
        return aDouble;
    }

    public void setaDouble(double aDouble) {
        this.aDouble = aDouble;
    }

    public BigDecimal getDecimal() {
        return decimal;
    }

    public void setDecimal(BigDecimal decimal) {
        this.decimal = decimal;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Reader getText() {
        return text;
    }

    public void setText(Reader text) {
        this.text = text;
    }

    public InputStream getBlob() {
        return blob;
    }

    public void setBlob(InputStream blob) {
        this.blob = blob;
    }

    @Override
    public String toString() {
        String line;

        StringBuilder textBuilder = new StringBuilder("null");
        BufferedReader bufferedReader;
        if (this.text != null) {
            bufferedReader = new BufferedReader(this.getText());
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    textBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder blobBuilder = new StringBuilder("null");
        if (this.blob != null) {
            bufferedReader = new BufferedReader(new InputStreamReader(this.getBlob()));
            try {
                while ((line = bufferedReader.readLine()) != null) {
                    blobBuilder.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "Test{" +
                "idtest=" + idtest +
                ", tinyint=" + tinyint +
                ", smallint=" + smallint +
                ", mediumint=" + mediumint +
                ", bigint=" + bigint +
                ", aFloat=" + aFloat +
                ", aDouble=" + aDouble +
                ", decimal=" + decimal +
                ", datetime=" + datetime +
                ", string='" + string + '\'' +
                ", text=" + textBuilder.toString() +
                ", blob=" + blobBuilder.toString() +
                '}';
    }
}
