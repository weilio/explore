package free.com.somanyapps;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable{

    public int     userId;
    public String  userName;

    public User(int userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pParcel, int pI) {
        pParcel.writeInt(userId);
        pParcel.writeString(userName);
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel pParcel) {
            return new User(pParcel);
        }

        @Override
        public User[] newArray(int pI) {
            return new User[pI];
        }
    };

    private User(Parcel in) {
        userId = in.readInt();
        userName = in.readString();
    }

    @Override
    public String toString() {
        return "userid:" + userId + ", username:" + userName;
    }
}
