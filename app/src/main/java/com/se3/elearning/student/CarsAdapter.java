////package com.se3.elearning.student;
////
////import com.se3.elearning.R;
////
////import android.content.Context;
////import android.view.LayoutInflater;
////import android.view.View;
////import android.view.ViewGroup;
////import android.widget.ArrayAdapter;
////import android.widget.TextView;
////
////import java.util.List;
////
////
////public class PlacesAdapter extends ArrayAdapter {
////    protected Context mContext;
////    protected List mPlaces;
////
////    public PlacesAdapter(Context context, List place) {
////        super(context, R.layout.fogot_tab_fragment, place);
////        mContext = context;
////        mPlaces = place;
////    }
////
////    @Override
////    public View getView(final int position, View convertView, ViewGroup parent) {
////        ViewHolder holder;
////
////        if (convertView == null) {
////            convertView = LayoutInflater.from(mContext).inflate(
////                    R.layout.fogot_tab_fragment, null);
////            holder = new ViewHolder();
////            holder.placeName = (TextView) convertView
////                    .findViewById(R.id.textView);
////            holder.placeDescription = (TextView) convertView
////                    .findViewById(R.id.textView);
////
////            convertView.setTag(holder);
////        } else {
////            holder = (ViewHolder) convertView.getTag();
////        }
////
////        ParseObject placesObject = mPlaces.get(position);
////
////        // title
////        String place = placesObject.getString("placeName");
////        holder.placeName.setText(place);
////
////        // content
////        String description = placesObject.getString("placeDescription");
////        holder.placeDescription.setText(description);
////
////        return convertView;
////    }
////
////    public static class ViewHolder {
////        TextView placeName;
////        TextView placeDescription;
////    }
////}
//
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.se3.elearning.R;String mTitle[] = {"Facebook", "whatsapp", "Twitter"};
//        ListView listView;
//public void onCreate(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//        mParam1 = getArguments().getString(ARG_PARAM1);
//        mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//        }
//
//
//@Override
//public View onCreateView(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_student_chat_room,
//        container, false);
//
//
//        listView = view.findViewById(R.id.listView);
//
//        //now we create our adapter class
//
//        MyAdapter adapter = new MyAdapter(getActivity(), mTitle);
//        listView.setAdapter(adapter);
//
//        //now set item click on list view
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//@Override
//public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        if (i == 0){
//        Toast.makeText(getContext(), "hello", Toast.LENGTH_SHORT).show();
//        }
//
//        }
//        });
//
//        return inflater.inflate(R.layout.fragment_student_chat_room, container, false);
//        }
//
//
//class MyAdapter extends ArrayAdapter<String> {
//    Context context;
//    String rTitile[];
//
//    MyAdapter(Context c, String title[]){
//        super(c, R.layout.fogot_tab_fragment, R.id.textView, title);
//        this.context = c;
//        this.rTitile = title;
//    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        LayoutInflater layoutInflater = (LayoutInflater)getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View row = layoutInflater.inflate(R.layout.fogot_tab_fragment, parent, false);
//        TextView myTittle = row.findViewById(R.id.textView);
//
//        myTittle.setText(rTitile[position]);
//        Toast.makeText(getContext(), rTitile[position], Toast.LENGTH_SHORT).show();
//        return row;
//    }
//}
