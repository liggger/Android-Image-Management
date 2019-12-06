### **一、Store a image**

```java
public void insertImg(int id , Bitmap img ) {   


    byte[] data = getBitmapAsByteArray(img); // this is a function

    insertStatement_logo.bindLong(1, id);       
    insertStatement_logo.bindBlob(2, data);

    insertStatement_logo.executeInsert();
    insertStatement_logo.clearBindings() ;

}

 public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    bitmap.compress(CompressFormat.PNG, 0, outputStream);       
    return outputStream.toByteArray();
}
```

```java
public Bitmap getImage(int i){

    String qu = "select img  from table where feedid=" + i ;
    Cursor cur = db.rawQuery(qu, null);

    if (cur.moveToFirst()){
        byte[] imgByte = cur.getBlob(0);
        cur.close();
        return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
    }
    if (cur != null && !cur.isClosed()) {
        cur.close();
    }       

    return null;
} 
```

### **二、将图片资源转为Bitmap**

```java
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.rest_work_stack, null);
```

```java
Bitmap bitmap = ((BitmapDrawable)getResources().getDrawable(R.mipmap.workstack)).getBitmap();
```

### **三、缩小图片**

```java
private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
}
```