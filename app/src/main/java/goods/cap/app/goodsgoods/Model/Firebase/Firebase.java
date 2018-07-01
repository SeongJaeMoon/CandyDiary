package goods.cap.app.goodsgoods.Model.Firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
//Firebase Instance 통일 필요 => (enum 활용 보류)
public class Firebase {
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static DatabaseReference postRef = database.getReference().child("posts");
    public static DatabaseReference likeRef = database.getReference().child("likes");
    public static DatabaseReference shareRef = database.getReference().child("shares");
    public static DatabaseReference starRef = database.getReference().child("stars");
    public static DatabaseReference userRef = database.getReference().child("users");
}
