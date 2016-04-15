package cn.minie.minie;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by cch on 16-4-15.
 */
public class ContactAdapter extends BaseAdapter {
    private Context context = null;
    private int resources;
    private ArrayList<HashMap<String, Object>> list = null;
    private String[] from;
    private int[] to;

    /**
     * 这里仿照的是SimpleAdapter的形参列表
     *
     * @param context
     * @param Resources
     * @param list
     * @param from
     * @param to
     */

    public ContactAdapter(Context context, int resources,
                     ArrayList<HashMap<String, Object>> list, String[] from, int[] to) {
        super();
        this.context = context;
        this.resources = resources;
        this.list = list;
        this.from = from;
        this.to = to;
    }

    /**
     * 剩下的问题就是依次实现BaseAdapter的这几个类方法就可以了
     */

    @Override
    public int getCount() {        //这个方法返回的是ListView的行数
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {      //这个方法没必要使用，可以用getItemId代替
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int itemId) {     //点击某一行时会调用该方法，其形参由安卓系统提供
        // TODO Auto-generated method stub
        return itemId;
    }

    /**
     * getView方法为系统在绘制每一行时调用，在此方法中要设置需要显示的文字，图片，
     * 以及为按钮设置监听器。
     * <p/>
     * 形参意义：
     * position：当前绘制的item 的位置（ID）；
     * convertView，系统在绘制ListView时，如果是绘制第一个Item（即第一行），convertView为null,当
     * 绘制第二个及以后的Item的convertView不为空，这时可以直接利用这个convertView的getTag()方法，获得各控件
     * 的实例，并进行相应的设置，这样可以加快绘图速度。
     * <p/>
     * 为了为convertView设置附加信息Tag，这里创建一个内部类ViewHolder，用于盛放一行中所有控件的引用，将这些引用
     * 实例化后作为convertView的附加信息。
     */
    class ViewHolder {
        public ImageButton ctPhoto = null;
        public TextView ctName = null, ctSign = null;

        /*
         * 从这里可以看出，from和to数组彼此之间的元素应该一一对应，同时from和to各自元素内部的顺序不同，最后ListView
         * 呈现的位置也会不同！
         */
        public ViewHolder(View convertView) {
            ctPhoto = (ImageButton) convertView.findViewById(to[0]);
            /*注意View和Activity都属于容器类，都需要设置布局文件，内部都含有子控件，且都有findViewById()
             * 他们之间没有明显的继承关系
             */
            ctName = (TextView) convertView.findViewById(to[1]);
            ctSign = (TextView) convertView.findViewById(to[2]);

        }

    }
    class ImageListener implements View.OnClickListener {

        private int position;

        public ImageListener(int position){
            this.position=position;
        }                          //构造函数没有返回值

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            String str=list.get(position).get(from[1]).toString();
            Toast.makeText(context,str+" is Clicked" , Toast.LENGTH_LONG).show();

        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        // TODO Auto-generated method stub

        /**
         * 首先判断是不是第一次创建Item，若是，则创建convertView实例和ViewHolder对象，并通过fandViewById()方法
         * 获得每一行中所有空间的实例放在ViewHolder对象中，然后对convertView设置标签
         */
        ViewHolder viewHolder=null;

        //注意convertView不是随意创建的，需要有LayoutInflater,根据list_item布局文件创建
        if(convertView==null){
            LayoutInflater inflater= LayoutInflater.from(context);
            convertView=inflater.inflate(resources,null);    //这里的null是一个ViewGroup形参，基本用不上
            viewHolder=new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder=(ViewHolder)convertView.getTag();    //通过getTag()方法获得附加信息
        }
        /**
         * 这里对viewHolder中的各个控件进行相应的设置
         */
        /**
         * @author DragonGN
         * 这里出现了一个问题：在绘制当前行的ListItem时，只需要对当前行的控件进行设置，因此这里不能加一个for
         * 循环对每一个list中的每一个元素进行遍历，而应该根据当前创建的ListItem行的position,然后
         * 访问数据库list中相应位置的Map的数据，进行控件的设置！
         */
        /**
         * 注意这里必须是setBackgroundDrawable() 而不是setBackground(),后者会报错，尽管前者过期了但一样可用
         */
        viewHolder.ctPhoto.setBackgroundDrawable((Drawable)(list.get(position).get(from[0])));
        //Map中要添加一个Drawable对象,这里的from和to中的元素应该一一对应,其顺序也应该对应ViewHolder构造方法中控件的调用的顺序

        viewHolder.ctName.setText((String)(list.get(position).get(from[1])));
        viewHolder.ctSign.setText((String)(list.get(position).get(from[2])));
        viewHolder.ctPhoto.setOnClickListener(new ImageListener(position));
        return convertView;     //把这个每一行的View对象返回
    }
}