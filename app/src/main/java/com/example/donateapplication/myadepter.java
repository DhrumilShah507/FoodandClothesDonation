package com.example.donateapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

//If want retrieve data from Realtime database extends-->  FirebaseRecyclerAdapter

public class myadepter extends RecyclerView.Adapter<myadepter.myviewholder> {


    Context context;


    public myadepter(ArrayList<model> datalist, Context context)
    {
        this.datalist = datalist;
        this.context = context;
    }


    ArrayList<model> datalist;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false); //use to add grid layout--> R.layout.grid_card_view
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {

        final model temp=datalist.get(position);

        holder.name.setText(datalist.get(position).getName());
        holder.email.setText(datalist.get(position).getEmail());
        holder.title.setText(datalist.get(position).getTitle());
        //holder.img.setImageResource(datalist.get(position).getImgname());

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,DataDisplayActivity.class);
                // intent.putExtra("imgname",temp.getImgname());
                intent.putExtra("Name",temp.getName());
                intent.putExtra("Email",temp.getEmail());
                intent.putExtra("Title",temp.getTitle());
                intent.putExtra("ADD",temp.getAdd());
                intent.putExtra("PickUpDate",temp.getPickupdate());
                intent.putExtra("PickUpTime",temp.getPickuptime());
                intent.putExtra("FoodType",temp.getFoodtype());
                intent.putExtra("FoodName",temp.getFoodname());
                intent.putExtra("Quantity",temp.getQuantity());
                intent.putExtra("District",temp.getDistrict());
                intent.putExtra("States",temp.getStates());
                intent.putExtra("Description",temp.getDescription());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }

    class myviewholder extends RecyclerView.ViewHolder
    {
        CircleImageView img;
        TextView name,email,title;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            img=(CircleImageView) itemView.findViewById(R.id.circleImageView);
            name=(TextView)itemView.findViewById(R.id.Name_txt);
            email=(TextView)itemView.findViewById(R.id.Email_txt);
            title=(TextView)itemView.findViewById(R.id.Title_txt);

        }
    }


}

