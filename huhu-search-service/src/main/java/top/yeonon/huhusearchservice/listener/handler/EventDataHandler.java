package top.yeonon.huhusearchservice.listener.handler;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 11:04
 **/
public interface EventDataHandler {

    /**
     * 处理写事件数据
     * @param data 数据
     */
    void handleWriteRowData(WriteRowsEventData data);

    /**
     * 处理更新事件数据
     * @param data 数据
     */
    void handleUpdateRowData(UpdateRowsEventData data);

    /**
     * 处理删除事件数据
     * @param data 数据
     */
    void handleDeleteRowData(DeleteRowsEventData data);
}
