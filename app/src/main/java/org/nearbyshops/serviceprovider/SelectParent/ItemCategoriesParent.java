package org.nearbyshops.serviceprovider.SelectParent;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import org.nearbyshops.serviceprovider.AddItems.ItemCategories.AddItemCategory;
import org.nearbyshops.serviceprovider.DaggerComponentBuilder;
import org.nearbyshops.serviceprovider.Model.ItemCategory;
import org.nearbyshops.serviceprovider.R;
import org.nearbyshops.serviceprovider.RetrofitRESTContract.ItemCategoryService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;

public class ItemCategoriesParent extends AppCompatActivity
        implements  ItemCategoriesParentAdapter.requestSubCategory, ItemCategoriesParentAdapter.NotificationReceiver{


    // data
    public static Map<Integer,ItemCategory> excludeList = new HashMap<>();
    List<ItemCategory> dataset = new ArrayList<>();

    boolean menuVisible = true;
    boolean instructionsVisible = false;

    @Inject
    ItemCategoryService itemCategoryService;

    ItemCategory currentCategory = null;


//    int currentCategoryID = 1; // the ID of root category is always supposed to be 1



//    private boolean isRootCategory = true;
//    private ArrayList<String> categoryTree = new ArrayList<>();




    // views
    RecyclerView itemCategoriesList;
    ItemCategoriesParentAdapter listAdapter;
    GridLayoutManager layoutManager;

    @Bind(R.id.show_hide_instructions)
    TextView showHideInstructions;

    @Bind(R.id.usage_instructions)
    TextView usageInstructions;

    @Bind(R.id.appbar)
    AppBarLayout appBarLayout;

    @Bind(R.id.assign_parent)
    TextView assignParent;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;






    public ItemCategoriesParent() {
        super();

        // Inject the dependencies using Dependency Injection
        DaggerComponentBuilder.getInstance()
                .getNetComponent().Inject(this);

        currentCategory = new ItemCategory();
        currentCategory.setItemCategoryID(1);
        currentCategory.setParentCategoryID(-1);
        currentCategory.setCategoryName("ROOT");
        currentCategory.setCategoryDescription("The root category.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_categories_parent);

        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        itemCategoriesList = (RecyclerView) findViewById(R.id.recyclerViewItemCategories);
        setupRecyclerView();
    }




    @OnClick(R.id.show_hide_instructions)
    void clickShowHideInstructions()
    {
        if(instructionsVisible)
        {
            usageInstructions.setVisibility(View.GONE);

            instructionsVisible = false;

        }else
        {
            usageInstructions.setVisibility(View.VISIBLE);

            instructionsVisible = true;
        }

    }


    void setupRecyclerView()
    {
        listAdapter = new ItemCategoriesParentAdapter(dataset,this,this,this);

        itemCategoriesList.setAdapter(listAdapter);

        layoutManager = new GridLayoutManager(this,1);

        itemCategoriesList.setLayoutManager(layoutManager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        layoutManager.setSpanCount(metrics.widthPixels/350);

        itemCategoriesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                if(newState==RecyclerView.SCROLL_STATE_DRAGGING)
//                {
//                    if(menuVisible)
//                    {


//                        appBarLayout.setVisibility(View.GONE);
//                        assignParent.setVisibility(View.GONE);

                        /*appBarLayout.animate()
                                .y(-appBarLayout.getHeight())
                                .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {

                                appBarLayout.setVisibility(View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }

                        });
*/

//                        menuVisible = false;
//                    }
//                    else
//                    {
//                        appBarLayout.setVisibility(View.VISIBLE);
//                        assignParent.setVisibility(View.VISIBLE);

                       /* appBarLayout.animate().translationY(0)
                                .setListener(new Animator.AnimatorListener() {

                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {


                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });
*/

//                        menuVisible = true;
//                    }
//
//
//                }
//            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);

                if(dy < -20)
                {

                    boolean previous = menuVisible;

                    menuVisible = true;

                    if(menuVisible !=previous)
                    {
                        // changed
//                        options.setVisibility(View.INVISIBLE);
                        Log.d("scrolllog","show");
//                        options.animate().translationX(metrics.widthPixels-10);

                        appBarLayout.setVisibility(View.VISIBLE);
                        assignParent.setVisibility(View.VISIBLE);



                    }

                }else if(dy > 20)
                {

                    boolean previous = menuVisible;

                    menuVisible = false;



                    if(menuVisible !=previous)
                    {
                        // changed
//                        options.setVisibility(View.VISIBLE);
//                        options.animate().translationX(0);
                        Log.d("scrolllog","hide");


                        appBarLayout.setVisibility(View.GONE);
                        assignParent.setVisibility(View.GONE);
                    }
                }

            }
        });

    }







    public void makeRequestRetrofit()
    {

        Call<List<ItemCategory>> itemCategoryCall = itemCategoryService
                .getItemCategories(currentCategory.getItemCategoryID());


        itemCategoryCall.enqueue(new Callback<List<ItemCategory>>() {


            @Override
            public void onResponse(Call<List<ItemCategory>> call, retrofit2.Response<List<ItemCategory>> response) {



                dataset.clear();

                if(response.body()!=null) {

                    if(currentCategory.getItemCategoryID()==1)
                    {
                        dataset.add(0,currentCategory);
                    }

                    // the entities in the exclude list should not be added into the dataset
                    for(ItemCategory itemCategory : response.body())
                    {
                        // if item does not exist in the exclude list then only add it.
                        if(!excludeList.containsKey(itemCategory.getItemCategoryID()))
                        {
                            dataset.add(itemCategory);
                        }
                    }

                }

                listAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<ItemCategory>> call, Throwable t) {

                showToastMessage("Network request failed. Please check your connection !");

            }
        });

    }



    private void showToastMessage(String message)
    {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onResume() {

        super.onResume();

        makeRequestRetrofit();

        Log.d("applog","curent category ID " + currentCategory.getItemCategoryID());
    }





    private void insertTab(String categoryName)
    {
        if(tabLayout.getVisibility()==View.GONE)
        {
            tabLayout.setVisibility(View.VISIBLE);
        }

        tabLayout.addTab(tabLayout.newTab().setText(" : : " + categoryName + " : : "));
        tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);

    }

    private void removeLastTab()
    {
        if(tabLayout.getTabCount()>0)
        {
            tabLayout.removeTabAt(tabLayout.getTabCount()-1);
            tabLayout.setScrollPosition(tabLayout.getTabCount()-1,0,true);
        }


        if(tabLayout.getTabCount()==0)
        {
            tabLayout.setVisibility(View.GONE);
        }
    }





    @Override
    public void notifyRequestSubCategory(ItemCategory itemCategory) {


        if(itemCategory.getItemCategoryID()==1)
        {
            return;
        }

        ItemCategory temp = currentCategory;
        currentCategory = itemCategory;
        currentCategory.setParentCategory(temp);

//        categoryTree.add(currentCategory.getCategoryName());

        insertTab(currentCategory.getCategoryName());

//        currentCategoryID = itemCategory.getItemCategoryID();

//        if(isRootCategory) {
//
//            isRootCategory = false;
//
//        }else
//        {
//            boolean isFirst = true;
//        }

        makeRequestRetrofit();
        appBarLayout.setVisibility(View.VISIBLE);
        assignParent.setVisibility(View.VISIBLE);

    }


    @Override
    public void onBackPressed() {


        Integer currentCategoryID = null;

        if(currentCategory!=null)
        {

//            if(categoryTree.size()>0) {
//
//                categoryTree.remove(categoryTree.size() - 1);
//
//            }

            removeLastTab();

            if(currentCategory.getParentCategory()!= null) {

                currentCategory = currentCategory.getParentCategory();
                currentCategoryID = currentCategory.getItemCategoryID();
            }
            else
            {
                currentCategoryID = currentCategory.getParentCategoryID();
            }


            if(currentCategoryID!=-1)
            {
                makeRequestRetrofit();
                appBarLayout.setVisibility(View.VISIBLE);
                assignParent.setVisibility(View.VISIBLE);

                listAdapter.clearSelection();
            }
        }

        if(currentCategoryID == -1)
        {
            super.onBackPressed();
        }

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();

        ButterKnife.unbind(this);
    }

    @Override
    public void notifyItemSelected() {

        assignParent.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyItemDeleted() {

        makeRequestRetrofit();
    }




    @OnClick(R.id.assign_parent)
    void assignParentClick()
    {
        if(listAdapter.getSelection()==null)
        {
            showToastMessage("No item selected. Please make a selection !");
            return;
        }


        Intent returnIntent = new Intent();
        returnIntent.putExtra("result",listAdapter.getSelection());
        setResult(Activity.RESULT_OK,returnIntent);

        // reset the static variable to null so that the data could be garbage collected.
        clearExcludeList();

        finish();

    }




    static public void clearExcludeList()
    {
        ItemCategoriesParent.excludeList.clear();
    }



}