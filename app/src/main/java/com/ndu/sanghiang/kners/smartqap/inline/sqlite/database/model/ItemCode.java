package com.ndu.sanghiang.kners.smartqap.inline.sqlite.database.model;

/*https://www.androidhive.info/2011/11/android-sqlite-database-tutorial/*/
public class ItemCode {
    public static final String TABLE_NAME = "ItemCode";

    //public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_CODE = "item_code";
    public static final String COLUMN_ITEM_DESC = "item_desc";
    public static final String COLUMN_SINGKATAN = "singkatan";
    public static final String COLUMN_KODE_SACHET = "kode_sachet";
    public static final String COLUMN_CAP_RASA = "cap_rasa";
    public static final String COLUMN_CAP_BOX = "cap_box";
    public static final String COLUMN_IS_KWG = "is_kwg";
    public static final String COLUMN_UMUR_PRODUK = "umur_produk";
    public static final String COLUMN_KODE_PABRIK = "kode_pabrik";
    public static final String COLUMN_KET = "ket";
    public static final String COLUMN_LOKAL_EXPORT = "lokal_export";
    public static final String COLUMN_GRAMASI_PERPCS = "gramasi_perpcs";
    public static final String COLUMN_JUMLAH_DOOS_PERBOX = "jumlah_doos_perbox";
    public static final String COLUMN_JUMLAH_PCS_PERBOX = "jumlah_pcs_perbox";
    public static final String COLUMN_TANGGAL_PIC_PENGUPDATE = "tanggal_pic_pengupdate";
    public static final String COLUMN_BARCODE = "barcode";
    public static final String COLUMN_TIMESTAMP = "timestamp";

    //private int id;
    private String item_code;
    private String item_desc;
    private String singkatan;
    private String kode_sachet;
    private String cap_rasa;
    private String cap_box;
    private String is_kwg;
    private String umur_produk;
    private String kode_pabrik;
    private String ket;
    private String lokal_export;
    private String gramasi_perpcs;
    private String jumlah_doos_perbox;
    private String jumlah_pcs_perbox;
    private String tanggal_pic_pengupdate;
    private String barcode;

    public ItemCode() {

    }

    public static String getColumnItemCode() {
        return COLUMN_ITEM_CODE;
    }

    public static String getColumnItemDesc() {
        return COLUMN_ITEM_DESC;
    }

    public static String getColumnSingkatan() {
        return COLUMN_SINGKATAN;
    }

    public static String getColumnKodeSachet() {
        return COLUMN_KODE_SACHET;
    }

    public static String getColumnCapRasa() {
        return COLUMN_CAP_RASA;
    }

    public static String getColumnCapBox() {
        return COLUMN_CAP_BOX;
    }

    public static String getColumnIsKwg() {
        return COLUMN_IS_KWG;
    }

    public static String getColumnUmurProduk() {
        return COLUMN_UMUR_PRODUK;
    }

    public static String getColumnKodePabrik() {
        return COLUMN_KODE_PABRIK;
    }

    public static String getColumnKet() {
        return COLUMN_KET;
    }

    public static String getColumnLokalExport() {
        return COLUMN_LOKAL_EXPORT;
    }

    public static String getColumnGramasiPerpcs() {
        return COLUMN_GRAMASI_PERPCS;
    }

    public static String getColumnJumlahDoosPerbox() {
        return COLUMN_JUMLAH_DOOS_PERBOX;
    }

    public static String getColumnJumlahPcsPerbox() {
        return COLUMN_JUMLAH_PCS_PERBOX;
    }

    public static String getColumnTanggalPicPengupdate() {
        return COLUMN_TANGGAL_PIC_PENGUPDATE;
    }

    public static String getColumnBarcode() {
        return COLUMN_BARCODE;
    }

    public static String getColumnTimestamp() {
        return COLUMN_TIMESTAMP;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {
        this.item_code = item_code;
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getSingkatan() {
        return singkatan;
    }

    public void setSingkatan(String singkatan) {
        this.singkatan = singkatan;
    }

    public String getKode_sachet() {
        return kode_sachet;
    }

    public void setKode_sachet(String kode_sachet) {
        this.kode_sachet = kode_sachet;
    }

    public String getCap_rasa() {
        return cap_rasa;
    }

    public void setCap_rasa(String cap_rasa) {
        this.cap_rasa = cap_rasa;
    }

    public String getCap_box() {
        return cap_box;
    }

    public void setCap_box(String cap_box) {
        this.cap_box = cap_box;
    }

    public String getIs_kwg() {
        return is_kwg;
    }

    public void setIs_kwg(String is_kwg) {
        this.is_kwg = is_kwg;
    }

    public String getUmur_produk() {
        return umur_produk;
    }

    public void setUmur_produk(String umur_produk) {
        this.umur_produk = umur_produk;
    }

    public String getKode_pabrik() {
        return kode_pabrik;
    }

    public void setKode_pabrik(String kode_pabrik) {
        this.kode_pabrik = kode_pabrik;
    }

    public String getKet() {
        return ket;
    }

    public void setKet(String ket) {
        this.ket = ket;
    }

    public String getLokal_export() {
        return lokal_export;
    }

    public void setLokal_export(String lokal_export) {
        this.lokal_export = lokal_export;
    }

    public String getGramasi_perpcs() {
        return gramasi_perpcs;
    }

    public void setGramasi_perpcs(String gramasi_perpcs) {
        this.gramasi_perpcs = gramasi_perpcs;
    }

    public String getJumlah_doos_perbox() {
        return jumlah_doos_perbox;
    }

    public void setJumlah_doos_perbox(String jumlah_doos_perbox) {
        this.jumlah_doos_perbox = jumlah_doos_perbox;
    }

    public String getJumlah_pcs_perbox() {
        return jumlah_pcs_perbox;
    }

    public void setJumlah_pcs_perbox(String jumlah_pcs_perbox) {
        this.jumlah_pcs_perbox = jumlah_pcs_perbox;
    }

    public String getTanggal_pic_pengupdate() {
        return tanggal_pic_pengupdate;
    }

    public void setTanggal_pic_pengupdate(String tanggal_pic_pengupdate) {
        this.tanggal_pic_pengupdate = tanggal_pic_pengupdate;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public static String getCreateTable() {
        return CREATE_TABLE;
    }


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    //+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_ITEM_CODE + " TEXT PRIMARY KEY,"
                    + COLUMN_ITEM_DESC + " TEXT,"
                    + COLUMN_SINGKATAN + " TEXT,"
                    + COLUMN_KODE_SACHET + " TEXT,"
                    + COLUMN_CAP_RASA + " TEXT,"
                    + COLUMN_CAP_BOX + " TEXT,"
                    + COLUMN_IS_KWG + " TEXT,"
                    + COLUMN_UMUR_PRODUK + " TEXT,"
                    + COLUMN_KODE_PABRIK + " TEXT,"
                    + COLUMN_KET + " TEXT,"
                    + COLUMN_LOKAL_EXPORT + " TEXT,"
                    + COLUMN_GRAMASI_PERPCS + " TEXT,"
                    + COLUMN_JUMLAH_DOOS_PERBOX + " TEXT,"
                    + COLUMN_JUMLAH_PCS_PERBOX + " TEXT,"
                    + COLUMN_TANGGAL_PIC_PENGUPDATE + " TEXT,"
                    + COLUMN_BARCODE + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public static String getTableName() {
        return TABLE_NAME;
    }

    public ItemCode(
            String itemCode,
            String itemDesc,
            String singkatan,
            String kodeSachet,
            String capRasa,
            String capBox,
            String isKwg,
            String umurProduk,
            String kodePabrik,
            String ket,
            String lokalExport,
            String gramasiPerpcs,
            String jumlahDoosPerbox,
            String jumlahPcsPerbox,
            String tanggalPicPengupdate,
            String barcode
    ) {
        //this.id = id;
        this.item_code = itemCode;
        this.item_desc = itemDesc;
        this.singkatan = singkatan;
        this.kode_sachet = kodeSachet;
        this.cap_rasa = capRasa;
        this.cap_box = capBox;
        this.is_kwg = isKwg;
        this.umur_produk = umurProduk;
        this.kode_pabrik = kodePabrik;
        this.ket = ket;
        this.lokal_export = lokalExport;
        this.gramasi_perpcs = gramasiPerpcs;
        this.jumlah_doos_perbox = jumlahDoosPerbox;
        this.jumlah_pcs_perbox = jumlahPcsPerbox;
        this.tanggal_pic_pengupdate = tanggalPicPengupdate;
        this.barcode = barcode;
    }
}
