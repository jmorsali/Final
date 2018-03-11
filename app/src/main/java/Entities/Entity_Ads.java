package Entities;

import com.loopj.android.http.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import Utility.UtilMethod;


public class Entity_Ads implements Serializable {

    private int Id;
    private int UserId;
    private String PictureUrl;
    private String Title;
    private String Price;
    private String PersianCreateDate;
    private String Description;
    private String Tel;
    private String MobileNo;
    private String Address;
    private String CreateDateTime;

    public String getCreateDateTime() {
        return CreateDateTime;
    }

    public void setCreateDateTime(String createDateTime) {
        CreateDateTime = createDateTime;
    }


    public int getUserId() {
        return UserId;
    }

    public int getId() {
        return Id;
    }

//    public byte[] getPicture_bytes() {
//        return Base64.decode(Picture, Base64.DEFAULT);
//    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public String getTitle() {
        return Title;
    }

    public String getPrice() {
        return Price;
    }

    public String getPersianCreateDate() {
        return PersianCreateDate;
    }

    public String getDescription() {
        return Description;
    }

    public String getTel() {
        return Tel;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public String getAddress() {
        return Address;
    }

    public Entity_Ads() {

    }

    /*
        public Entity_Ads(JSONObject obj) throws JSONException {

            this.Id = obj.getInt("Id");
            this.UserId = obj.getInt("UserId");
            this.Title = obj.getString("Title");
            this.Price = obj.getString("Price");
            this.Address = obj.getString("Address");
            this.MobileNo = obj.getString("MobileNo");
            this.Tel = obj.getString("Tel");
            this.PersianCreateDate = obj.getString("PersianCreateDate");
            this.Description = obj.getString("Description");

            this.Picture = obj.getString("Picture");

        }

    */
    public void setTitle(CharSequence title) {
        this.Title = title.toString();
    }

    public void setPrice(CharSequence price) {
        this.Price = price.toString();
    }

    public void setAddress(CharSequence address) {
        this.Address = address.toString();
    }

    public void setDescription(CharSequence description) {
        this.Description = description.toString();
    }

    public void setMobileNo(CharSequence mobile) {
        this.MobileNo = mobile.toString();
    }

    public void setTel(CharSequence tel) {
        this.Tel = tel.toString();
    }

    public void setPersianCreateDate(String persianCreateDate) {
        this.PersianCreateDate = persianCreateDate;
    }

    public void setPictureUrl(String pictureurl) {
        this.PictureUrl = pictureurl;
    }

    public void setUserId(int userId) {
        this.UserId = userId;
    }
}
