package fr.univcotedazur.multifidelitycards.controllers.mapper;

import fr.univcotedazur.multifidelitycards.controllers.dto.ScheduleDTO;
import fr.univcotedazur.multifidelitycards.controllers.dto.StoreDTO;
import fr.univcotedazur.multifidelitycards.entities.Schedule;
import fr.univcotedazur.multifidelitycards.entities.Store;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StoreMapper {

    public StoreDTO toDto(Store store){
        return new StoreDTO(store.getId(), store.getName(), store.getAddress(),
                toDtosSchedule(store.getSchedule()), store.getZone().getName());
    }

    public List<StoreDTO> toDtos(List<Store> stores){
        return stores.stream().map(this::toDto).collect(Collectors.toList());
    }

    public ScheduleDTO scheduleDTO(Schedule schedule){
        return new ScheduleDTO(schedule.getId(),schedule.getBeginTime(), schedule.getEndTime(),
                schedule.getDayOfWeek());
    }

    public List<ScheduleDTO> toDtosSchedule(List<Schedule> schedules){
        if(schedules == null) return null;
        return schedules.stream().map(this::scheduleDTO).collect(Collectors.toList());
    }

}