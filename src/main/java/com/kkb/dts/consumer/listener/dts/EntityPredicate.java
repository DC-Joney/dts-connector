package com.kkb.dts.consumer.listener.dts;

import com.alibaba.dts.formats.avro.Operation;
import com.alibaba.dts.formats.avro.Record;
import com.kkb.dts.consumer.listener.EntityInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;

@Slf4j
public class EntityPredicate implements BiPredicate<Record, EntityInfo<?>> {

    @Override
    public boolean test(Record record, EntityInfo<?> entityInfo) {
        String dtsTableName = record.getObjectName();

        //从dts中获取对应的表名称
        if (StringUtils.hasText(dtsTableName)) {
            dtsTableName = Optional.of(dtsTableName.split("\\."))
                    .filter(array -> array.length > 1).map(array -> array[1]).orElse(null);
        }

        boolean state = StringUtils.hasText(dtsTableName) && Objects.nonNull(record.getOperation()) &&
                dtsTableName.equals(entityInfo.getTableName())
                && (record.getOperation().equals(Operation.INSERT)
                || record.getOperation().equals(Operation.UPDATE)
                || record.getOperation().equals(Operation.DELETE));

        if (state)
            log.info("Record is support currentTable, record info: [table: {}, operation: {}], currentTable: {}",
                    record.getObjectName(), record.getOperation(), entityInfo.getTableName());

        return state;
    }
}
