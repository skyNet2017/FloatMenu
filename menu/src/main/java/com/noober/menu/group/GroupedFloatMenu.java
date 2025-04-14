package com.noober.menu.group;

import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import com.noober.menu.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupedFloatMenu extends PopupWindow {

    private Context context;
    private List<MenuGroup> menuGroups;

    public GroupedFloatMenu(Context context, List<MenuGroup> menuGroups) {
        super(context);
        this.context = context;
        this.menuGroups = menuGroups;
        init();
    }

    private void init() {
        // 新增：计算屏幕宽度并限制最大宽度
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int maxWidth = (int) (screenWidth * 0.9f); // 设置宽度为屏幕的70%

        // 新增：包裹滚动容器
        ScrollView scrollView = new ScrollView(context);
        scrollView.setScrollBarSize(0); // 隐藏滚动条
        scrollView.setBackgroundColor(Color.TRANSPARENT); // 保持透明背景

        // 修改：主布局设置最大宽度
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(20, 20, 20, 20);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                maxWidth, // 限制最大宽度
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        for (MenuGroup group : menuGroups) {
            // 添加组名
            TextView groupNameView = new TextView(context);
            groupNameView.setText(group.getGroupName());
            groupNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            groupNameView.setTextColor(Color.BLACK);
            //设置粗体
            groupNameView.setTypeface(null, android.graphics.Typeface.BOLD);
            groupNameView.setPadding(20, 10, 0, 10);
            mainLayout.addView(groupNameView);

            // 添加 GridLayout
            FlexboxLayout flexLayout = new FlexboxLayout(context);
            flexLayout.setFlexWrap(FlexWrap.WRAP); // 启用自动换行
            flexLayout.setFlexDirection(FlexDirection.ROW); // 水平排列
            flexLayout.setAlignItems(AlignItems.STRETCH); // 垂直拉伸对齐

            for (MenuItem menuItem : group.getItems()) {
                TextView itemTextView = new TextView(context);
                itemTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                itemTextView.setText(menuItem.getItem());
                itemTextView.setGravity(Gravity.CENTER);
                itemTextView.setPadding(20, 20, 20, 20);
                //itemTextView.setBackgroundResource(android.R.drawable.btn_default);
                itemTextView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.selector_item));
                itemTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dismiss();
                        menuItem.getOnClickListener().onClick(view);
                    }
                });

                // 使用FlexboxLayout的布局参数
                FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, // 宽度为0配合flex属性
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(5, 5, 5, 5);
                //params.setFlexGrow(1.0f); // 平均分配空间
               // params.setFlexBasisPercent(0.32f); // 每个占约1/3宽度（含边距）
                itemTextView.setLayoutParams(params);

                flexLayout.addView(itemTextView);
            }

            mainLayout.addView(flexLayout);
        }

        // 新增：将布局添加到滚动容器
        scrollView.addView(mainLayout);
        mainLayout.setBackgroundColor(Color.WHITE); // 设置背景颜色
        scrollView.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.bg_shadow));
        // 修改：设置内容视图为滚动容器
        setContentView(scrollView);

        // 修改：设置窗口尺寸
        setWidth(maxWidth); // 固定最大宽度
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT); // 自适应高度

        setFocusable(true);
        setBackgroundDrawable(null);
    }

    public static class MenuGroup {
        private String groupName;
        private List<MenuItem> items;

        public MenuGroup(String groupName, List<MenuItem> items) {
            this.groupName = groupName;
            this.items = items;
        }

        public String getGroupName() {
            return groupName;
        }

        public List<MenuItem> getItems() {
            return items;
        }
    }

    public static class MenuItem {
        private String item;
        private View.OnClickListener onClickListener;

        public MenuItem(String item, View.OnClickListener onClickListener) {
            this.item = item;
            this.onClickListener = onClickListener;
        }

        public String getItem() {
            return item;
        }

        public View.OnClickListener getOnClickListener() {
            return onClickListener;
        }
    }
}