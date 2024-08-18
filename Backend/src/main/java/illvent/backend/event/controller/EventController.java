package illvent.backend.event.controller;

import illvent.backend.event.domain.DateFilter;
import illvent.backend.event.dto.EventInfoResponseDTO;
import illvent.backend.event.dto.EventRegisterRequestDTO;
import illvent.backend.event.dto.EventResponseDTO;
import illvent.backend.event.dto.EventUpdateRequestDTO;
import illvent.backend.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Event", description = "행사 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/event")
public class EventController {

    private final EventService eventService;

    @Operation(summary = "행사를 등록하는 API")
    @PostMapping("/register")
    public ResponseEntity<String> registerEvent(@RequestBody EventRegisterRequestDTO eventRegisterRequestDTO) {
        eventService.registerEvent(eventRegisterRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "특정 행사 정보를 수정하는 API")
    @PutMapping("/update/{eventNo}")
    public ResponseEntity<EventResponseDTO> updateEvent(@PathVariable Long eventNo, @RequestBody EventUpdateRequestDTO eventUpdateRequestDTO) {
        EventResponseDTO event = eventService.updateEvent(eventNo, eventUpdateRequestDTO);

        return ResponseEntity.ok(event);
    }

    @Operation(summary = "특정 행사 정보를 삭제하는 API")
    @DeleteMapping("/delete/{eventNo}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long eventNo) {
        eventService.deleteEvent(eventNo);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "특정 행사 정보를 반환하는 API")
    @GetMapping("/{eventNo}")
    public ResponseEntity<EventResponseDTO> getEvent(@PathVariable Long eventNo) {
        EventResponseDTO event = eventService.getEvent(eventNo);

        return ResponseEntity.ok(event);
    }

    @Operation(summary = "모든 행사 정보를 반환하는 API")
    @GetMapping("/list")
    public ResponseEntity<List<EventResponseDTO>> getAllEvents() {
        List<EventResponseDTO> events = eventService.getAllEvents();

        return ResponseEntity.ok(events);
    }

    @Operation(summary = "조회수를 기준으로 상위 10개의 행사 정보를 반환하는 API")
    @GetMapping("/list/top")
    public ResponseEntity<List<EventResponseDTO>> getTopEventsOrderByViews() {
        List<EventResponseDTO> events = eventService.getEventsOrderByViews();

        return ResponseEntity.ok(events);
    }

    @Operation(summary = "여러 필터를 기준에 맞는 행사 정보를 반환하는 API")
    @GetMapping("")
    public ResponseEntity<List<EventInfoResponseDTO>> getEventsByFilter(@RequestParam(value="date",required = false) DateFilter date,
                                                                        @RequestParam(value="region",required = false) String region,
                                                                        @RequestParam(value = "join",required = false)String join,
                                                                        @RequestParam(value="price",required = false) String price,
                                                                        @RequestParam(value="page",defaultValue ="0") int page,
                                                                        @RequestParam(value = "size",defaultValue = "9") int size) {
        System.out.println("date: " + date);
        System.out.println("region: " + region);
        System.out.println("join: " + join);
        System.out.println("price: " + price);

        if(region.equals("전체")){
            region = null;
        }
        if(join.equals("onoff")){
            join = null;
        }
        if(price.equals("freeAndPaid")){
            price = null;
        }

        List<EventInfoResponseDTO> result = eventService.getEventsByFilter(date,region,join,price,page,size);

        return ResponseEntity.ok(result);
    }

}
