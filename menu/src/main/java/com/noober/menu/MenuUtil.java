package com.noober.menu;

import android.text.TextUtils;
import android.view.View;
import android.widget.PopupWindow;

import com.noober.menu.group.GroupedFloatMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: Administrator
 * @date: 2022/2/2
 * @desc: //todo
 */
public class MenuUtil {

    public static <T> void showMenu(View targetView, List<IMenu<T>> menus, T info) {
        // 按 groupName 分组
        Map<String, List<IMenu<T>>> groupedMenus = new HashMap<>();
        boolean allEmpty = true;
        for (IMenu<T> menu : menus) {
            String groupName = menu.groupName(); // 假设 IMenu 接口新增了 groupName 方法
            if(TextUtils.isEmpty(groupName)){
                groupName = "默认分组";
            }else {
                allEmpty = false;
            }
            if (!groupedMenus.containsKey(groupName)) {
                groupedMenus.put(groupName, new ArrayList<>());
            }
            groupedMenus.get(groupName).add(menu);
        }

        if(allEmpty){
            // 如果所有菜单都没有分组名称，则直接使用原来的方式显示菜单
            String[] desc = new String[menus.size()];
            for (int i = 0; i < menus.size(); i++) {
                desc[i] = menus.get(i).text();
            }
            FloatMenu floatMenu = new FloatMenu(targetView.getContext(), targetView);
            floatMenu.items(desc);
            floatMenu.setOnItemClickListener(new FloatMenu.OnItemClickListener() {
                @Override
                public void onClick(View v, int position) {
                    menus.get(position).onMenuClicked(position, info);
                }
            });

            showAtBottomOrTop(targetView, floatMenu);
            return;
        }


        // 构造 GroupedFloatMenu 所需的数据结构
        List<GroupedFloatMenu.MenuGroup> menuGroups = new ArrayList<>();
        for (Map.Entry<String, List<IMenu<T>>> entry : groupedMenus.entrySet()) {
            String groupName = entry.getKey();
            List<GroupedFloatMenu.MenuItem> menuItems = new ArrayList<>();
            for (IMenu<T> menu : entry.getValue()) {
                menuItems.add(new GroupedFloatMenu.MenuItem(menu.text(), v -> menu.onMenuClicked(-1, info)));
            }
            menuGroups.add(new GroupedFloatMenu.MenuGroup(groupName, menuItems));
        }

        // 显示分组菜单
        GroupedFloatMenu groupedFloatMenu = new GroupedFloatMenu(targetView.getContext(), menuGroups);
        showAtBottomOrTop(targetView, groupedFloatMenu);

    }

    private static void showAtBottomOrTop(View targetView, PopupWindow floatMenu) {
        int[] location = new int[2];
        targetView.getLocationOnScreen(location);
        int screenHeight = targetView.getResources().getDisplayMetrics().heightPixels;
        int targetViewY = location[1];
        //打印
        System.out.println("targetViewY: " + targetViewY);
        System.out.println("screenHeight: " + screenHeight);
        if (targetViewY > screenHeight / 2) {
            // 如果超过屏幕高度的一半，则将菜单显示在上方
            floatMenu.showAsDropDown(targetView, 0, -targetViewY);
        } else {
            // 否则显示在下方
            floatMenu.showAsDropDown(targetView);
        }
    }


    public interface IMenu<T>{
        String text();
       default String groupName() {
           return "";
       }
        void  onMenuClicked(int position,T bean);
    }
}
