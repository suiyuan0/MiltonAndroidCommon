
package com.milton.common.demo.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.milton.common.demo.av.Student;
import com.milton.common.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Fragment2LeanCloud extends Fragment2Base {
    private final static String TAG = Fragment2LeanCloud.class.getSimpleName();

    public Class[] getItemClass() {
        return new Class[]{
                // UtilsScreenActivity.class,
                // UtilsContextActivity.class,
                // UtilsResourceActivity.class,
                // UtilsStringActivity.class,
                // UtilsNetActivity.class,
                // UtilsNotificationActivity.class,
        };
    }

    public String[] getItemNames() {
        return new String[]{
                "testCreateObject",
                "testCreateObject2",
                "testCreateObject3",
                "testCreateObject4",
                "testQuery",
                "testUpdate",
                "testDelete",
                "testBatchOperation",
                "testAVRelation",
                "testPointer",
                "",
                "",
                "",
                "",

        };
    }

    @Override
    public void onItemClick(AdapterView<?> adapterview, View view, int i, long l) {
        // getActivity().startActivity(new Intent(getActivity(), mClassList[i]));
        switch (i) {
            case 0:
                testCreateObject();
                break;
            case 1:
                testCreateObject2();
                break;
            case 2:
                testCreateObject3();
                break;
            case 3:
                testCreateObject4();
                break;
            case 4:
                testQuery();
                break;

            case 5:
                testUpdate();
                break;
            case 6:
                testDelete();
                break;
            case 7:
                testBatchOperation();
                break;
            case 8:
                testAVRelation();
                break;
            case 9:
                testPointer();
                break;
            case 10:

                break;
            case 11:

                break;
            case 12:

                break;

            default:
                break;
        }
    }

    public void testCreateObject() {
        try {
            Student student = new Student();
            student.setAge(12);
            student.setName("Mike");
            student.saveInBackground();
//    log("保存了一个学生：" + prettyJSON(student));
//    logThreadTips();
        } catch (Exception e) {
            LogUtil.e("TAG", "AVException is " + e.getMessage());
        }
    }

    public void testCreateObject3() {
        boolean bool = true;
        int number = 2015;
        String string = number + " 年度音乐排行";
        Date date = new Date();

        byte[] data = "短篇小说".getBytes();
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(number);
        arrayList.add(string);
        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("数字", number);
        hashMap.put("字符串", string);

        AVObject object = new AVObject("DataTypes");
        object.put("testBoolean", bool);
        object.put("testInteger", number);
        object.put("testDate", date);
        object.put("testData", data);
        object.put("testArrayList", arrayList);
        object.put("testHashMap", hashMap);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                LogUtil.e("TAG", e == null ? "Add Success" : "AVException is " + e.getMessage());
            }
        });
    }

    public void testCreateObject2() {
        final AVObject todo = new AVObject("Todo");
        todo.put("title", "工程师周会");
        todo.put("content", "每周工程师会议，周一下午2点");
        todo.put("location", "会议室");// 只要添加这一行代码，服务端就会自动添加这个字段
        todo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    LogUtil.e("TAG", "Add Success " + todo.getObjectId());
                    // 存储成功
                } else {
                    LogUtil.e("TAG", "AVException is " + e.getMessage());
                    // 失败的话，请检查网络环境以及 SDK 配置是否正确
                }
            }
        });
    }

    public void testCreateObject4() {
        // 执行 CQL 语句实现新增一个 TodoFolder 对象
        AVQuery.doCloudQueryInBackground("insert into TodoFolder(name, priority) values('工作', 1)", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                LogUtil.e("TAG", e == null ? "Add Success" : "AVException is " + e.getMessage());
            }
        });
    }

    public void testQuery() {
//        testQuery1();
        testQuery2();
//        testQuery3();
//        testQuery4();
//        testQuery5();
    }

    public void testQuery1() {
        AVQuery<AVObject> avQuery = new AVQuery<>("Todo");
        avQuery.getInBackground("57d65cef2e958a00546dc943", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是 id 为 57d65cef2e958a00546dc943 的 Todo 对象实例
                if (e == null) {
                    LogUtil.e(TAG, avObject == null ? "avObject is null" : "avObject = " + avObject.toString());
                } else {
                    LogUtil.e("TAG", "AVException is " + e.getMessage());
                }

            }
        });
    }


    public void testQuery2() {
        AVObject todo = AVObject.createWithoutData("Todo", "57d65cef2e958a00546dc943");
        todo.fetchInBackground(new GetCallback<AVObject>() {
                                   @Override
                                   public void done(AVObject avObject, AVException e) {
                                       if (e == null) {
                                           LogUtil.e(TAG, avObject == null ? "avObject is null" : "avObject = " + avObject.toString());
                                       } else {
                                           LogUtil.e("TAG", "AVException is " + e.getMessage());
                                       }
                                   }
                               }

        );
    }

    public void testQuery3() {
        AVQuery<AVObject> avQuery = new AVQuery<>("Todo");
        avQuery.getInBackground("57d65cef2e958a00546dc943", new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // object 就是 id 为 558e20cbe4b060308e3eb36c 的 Todo 对象实例

                int priority = avObject.getInt("priority");
                String location = avObject.getString("location");
                String title = avObject.getString("title");
                String content = avObject.getString("content");

                // 获取三个特殊属性
                String objectId = avObject.getObjectId();
                Date updatedAt = avObject.getUpdatedAt();
                Date createdAt = avObject.getCreatedAt();
            }
        });
    }


    public void testQuery4() {
        // 假如已知了 objectId 可以用如下的方式构建一个 AVObject
        AVObject anotherTodo = AVObject.createWithoutData("Todo", "57d65cef2e958a00546dc943");
        // 然后调用刷新的方法，将数据从服务端拉到本地
        anotherTodo.fetchIfNeededInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // 调用 fetchIfNeededInBackground 和 refreshInBackground 效果是一样的。
            }
        });
    }

    public void testQuery5() {
        AVObject theTodo = AVObject.createWithoutData("Todo", "57d65cef2e958a00546dc943");
        String keys = "priority,location";// 指定刷新的 key 字符串
        theTodo.fetchInBackground(keys, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                // theTodo 的 location 和 content 属性的值就是与服务端一致的
                String priority = avObject.getString("priority");
                String location = avObject.getString("location");
            }
        });
//刷新操作会强行使用云端的属性值覆盖本地的属性。因此如果本地有属性修改，请慎用这类接口
    }

    public void testQuery6() {

    }

    public void testUpdate() {
        testUpdate1();
        testUpdate2();
        testUpdate3();
        testUpdate4();
        testUpdate5();

    }

    public void testUpdate1() {
        // 第一参数是 className,第二个参数是 objectId
        AVObject todo = AVObject.createWithoutData("Todo", "57d65cef2e958a00546dc943");

        // 修改 content
        todo.put("content", "每周工程师会议，本周改为周三下午3点半。");
        // 保存到云端
        todo.saveInBackground();
    }

    public void testUpdate2() {
        // 执行 CQL 语句实现更新一个 TodoFolder 对象
        AVQuery.doCloudQueryInBackground("update TodoFolder set name='家庭' where objectId='57d65fb28ac247006207f3ca'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                // 如果 e 为空，说明保存成功
            }
        });
    }

    public void testUpdate3() {
        final AVObject theTodo = AVObject.createWithoutData("Todo", "57d65cef2e958a00546dc943");
        theTodo.put("views", 0);//初始值为 0
        theTodo.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                // 原子增加查看的次数
                theTodo.increment("views");
                theTodo.setFetchWhenSave(true);
                theTodo.saveInBackground();
                // 也可以使用 incrementKey:byAmount: 来给 Number 类型字段累加一个特定数值。
                theTodo.increment("views", 5);
                theTodo.saveInBackground();
                //saveInBackground 调用之后，如果成功的话，对象的计数器字段是当前系统最新值。
            }
        });
    }

    public void testUpdate4() {
        addReminders();
    }

    Date getDateWithDateString(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    void addReminders() {
        Date reminder1 = getDateWithDateString("2015-11-11 07:10:00");
        Date reminder2 = getDateWithDateString("2015-11-11 07:20:00");
        Date reminder3 = getDateWithDateString("2015-11-11 07:30:00");

        AVObject todo = new AVObject("Todo");
        todo.addAllUnique("reminders", Arrays.asList(reminder1, reminder2, reminder3));
        todo.saveInBackground();
    }

    public void testUpdate5() {

    }

    public void testDelete() {
        testDelete1();
        testDelete2();
        testDelete3();
    }

    public void testDelete1() {
        // 执行 CQL 语句实现删除一个 Todo 对象
        AVQuery.doCloudQueryInBackground("delete from Todo where objectId='57d659972e958a00546dab3f'", new CloudQueryCallback<AVCloudQueryResult>() {
            @Override
            public void done(AVCloudQueryResult avCloudQueryResult, AVException e) {
                // 如果 e 为空，说明保存成功
            }
        });
    }

    public void testDelete2() {
        AVObject todo = AVObject.createWithoutData("Todo", "57d66b02816dfa00544e885a");
        todo.deleteInBackground();
    }

    public void testDelete3() {

    }

    public void testBatchOperation() {
        AVQuery<AVObject> query = new AVQuery<>("Todo");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                List<AVObject> todos = list;
                for (AVObject todo : list) {
                    todo.put("status", 1);
                }

                AVObject.saveAllInBackground(todos, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            // 出现错误
                        } else {
                            // 保存成功
                        }
                    }
                });
            }
        });
    }

    public void testAVRelation() {
        final AVObject todoFolder = new AVObject("TodoFolder");// 构建对象
        todoFolder.put("name", "工作");
        todoFolder.put("priority", 1);

        final AVObject todo1 = new AVObject("Todo");
        todo1.put("title", "工程师周会");
        todo1.put("content", "每周工程师会议，周一下午2点");
        todo1.put("location", "会议室");

        final AVObject todo2 = new AVObject("Todo");
        todo2.put("title", "维护文档");
        todo2.put("content", "每天 16：00 到 18：00 定期维护文档");
        todo2.put("location", "当前工位");

        final AVObject todo3 = new AVObject("Todo");
        todo3.put("title", "发布 SDK");
        todo3.put("content", "每周一下午 15：00");
        todo3.put("location", "SA 工位");

        AVObject.saveAllInBackground(Arrays.asList(todo1, todo2, todo3), new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVRelation<AVObject> relation = todoFolder.getRelation("containedTodos");// 新建一个 AVRelation
                relation.add(todo1);
                relation.add(todo2);
                relation.add(todo3);
                // 上述 3 行代码表示 relation 关联了 3 个 Todo 对象

                todoFolder.saveInBackground();
            }
        });
    }

    public void testPointer() {
        AVObject comment = new AVObject("Comment");// 构建 Comment 对象
        comment.put("like", 1);// 如果点了赞就是 1，而点了不喜欢则为 -1，没有做任何操作就是默认的 0
        comment.put("content", "这个太赞了！楼主，我也要这些游戏，咱们团购么？");// 留言的内容

        // 假设已知了被分享的该 TodoFolder 的 objectId 是 5590cdfde4b00f7adb5860c8
        comment.put("targetTodoFolder", AVObject.createWithoutData("TodoFolder", "57d65fb28ac247006207f3ca"));
        // 以上代码就是的执行结果就会在 comment 对象上有一个名为 targetTodoFolder 属性，它是一个 Pointer 类型，指向 objectId 为 5590cdfde4b00f7adb5860c8 的 TodoFolder
    }
}
