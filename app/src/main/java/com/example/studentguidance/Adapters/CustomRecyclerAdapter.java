package com.example.studentguidance.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.studentguidance.ModelClasses.Faculty;
import com.example.studentguidance.R;
import com.example.studentguidance.activities.MainActivity;
import com.example.studentguidance.fragments.ShowFacultyInfoFragment;
import com.example.studentguidance.fragments.UpdateFacultyInfoFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import org.parceler.Parcel;
import org.parceler.Parcels;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder> {
    Context context;
    ArrayList<Faculty> facultyArrayList;
    FragmentManager fragmentManager;







    public CustomRecyclerAdapter(Context context, ArrayList<Faculty> facultyArrayList,FragmentManager fragmentManager){
        this.context=context;
        this.facultyArrayList=facultyArrayList;
        this.fragmentManager=fragmentManager;
    }











    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }





    






    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(facultyArrayList.get(position).getImagelink())
                .placeholder(R.drawable.person_image)
                .into(holder.facultyImage);
        holder.nameText.setText(facultyArrayList.get(position).getName());
        holder.designationText.setText(facultyArrayList.get(position).getDesignation());
        holder.optionsMoreIV.setVisibility((MainActivity.SUCCESSFUL_LOGIN)?View.VISIBLE:View.INVISIBLE);
        holder.optionsMoreIV.setOnClickListener(v->showPopupMenu(v,holder));
        holder.recyclerItemLL.setOnClickListener(v->{
            ShowFacultyInfoFragment sf=new ShowFacultyInfoFragment();
            Bundle bundle = new Bundle();
            Parcelable parcelable=Parcels.wrap(facultyArrayList.get(position));
            bundle.putParcelable("GET_FACULTY_FROM_ADAPTER",parcelable);
            sf.setArguments(bundle);
            loadFragment(sf,1);
        });

    }









    @Override
    public int getItemCount() {
        return facultyArrayList.size();
    }












    static class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView facultyImage;
        TextView nameText,designationText;
        ImageView optionsMoreIV;
        LinearLayout recyclerItemLL;





        public MyViewHolder(@NonNull View item) {
            super(item);
            facultyImage=item.findViewById(R.id.facultyImage);
            nameText=item.findViewById(R.id.nameText);
            designationText=item.findViewById(R.id.designationText);
            optionsMoreIV=item.findViewById(R.id.optionsMoreIV);
            recyclerItemLL=item.findViewById(R.id.recyclerItemLL);
        }
    }














    private void showPopupMenu(View view,MyViewHolder holder){
        PopupMenu popupMenu = new PopupMenu(context,view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_modify,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.editItem: {
                    UpdateFacultyInfoFragment uf=new UpdateFacultyInfoFragment();
                    Parcelable parcelable= Parcels.wrap(facultyArrayList.get(holder.getAdapterPosition()));
                    Bundle bundle=new Bundle();
                    bundle.putParcelable("FACULTY_OBJ",parcelable);
                    uf.setArguments(bundle);
                    loadFragment(uf,1);
                    return true;
                }
                case R.id.deleteItem:{
                    FirebaseFirestore db=FirebaseFirestore.getInstance();
                    db.collection("StudentGuidanceFacultyInfo").
                            document(facultyArrayList.get(holder.getAdapterPosition()).getDocumentid()).delete()
                            .addOnFailureListener(e -> {
                                Toast.makeText(context, "Deletion unsuccessful", Toast.LENGTH_SHORT).show();
                            });
                    return true;
                }
                default: return false;
            }
        });
        showMenuIcons(popupMenu);
        popupMenu.show();
    }
















    private void showMenuIcons(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
















    private void loadFragment(Fragment fragment,int flag){
        FragmentTransaction ft=fragmentManager.beginTransaction();
        if(flag==0) {
            ft.add(R.id.container,fragment).commit();
            fragmentManager.popBackStack(MainActivity.ROOT_FRAGMENT_TAG,FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else {
            ft.replace(R.id.container,fragment).commit();
            ft.addToBackStack(null);
        }
    }
}
