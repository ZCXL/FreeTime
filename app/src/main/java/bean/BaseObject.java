package bean;

/**
 * Created by zhuchao on 7/12/15.
 */
public class BaseObject {
    public static enum  TYPE{
        USER(0),VERSION(1),MOVIE(2);
        private int value;
        private TYPE(int value){
            this.value=value;
        }
        public static TYPE valueOf(int value){
            switch (value){
                case 0:
                    return USER;
                case 1:
                    return VERSION;
                case 2:
                    return MOVIE;
                default:
                    return null;
            }
        }
        public int value(){
            return this.value;
        }
    }
    private TYPE type;
    public BaseObject(TYPE type){
        this.type=type;
    }
}
