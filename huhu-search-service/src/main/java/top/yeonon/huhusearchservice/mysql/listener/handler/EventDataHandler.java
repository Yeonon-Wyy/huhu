package top.yeonon.huhusearchservice.mysql.listener.handler;

import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import top.yeonon.huhucommon.exception.HuhuException;

/**
 * @Author yeonon
 * @date 2019/5/2 0002 11:04
 **/
public interface EventDataHandler {

    /**
     * 处理写事件数据
     * @param data 数据
     */
    void handleWriteRowData(WriteRowsEventData data)
        throws HuhuException;

    /**
     * 处理更新事件数据
     * @param data 数据
     */
    void handleUpdateRowData(UpdateRowsEventData data)
        throws HuhuException;

    /**
     * 处理删除事件数据
     * @param data 数据
     */
    void handleDeleteRowData(DeleteRowsEventData data)
        throws HuhuException;
}
