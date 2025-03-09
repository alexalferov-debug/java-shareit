package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemRequestAddDto;
import ru.practicum.shareit.item.dto.comment.AddCommentDto;
import ru.practicum.shareit.item.dto.comment.CommentDto;
import ru.practicum.shareit.user.dto.UserDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@EnableCaching
public class ItemValidationTests {
    private static final String USER_HEADER = "X-Sharer-User-Id";
    @MockBean
    private ItemClient itemClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    ItemRequestAddDto item;
    ItemDto itemResponse;
    CommentDto commentDto;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");

    @BeforeEach
    public void setup() {
        item = new ItemRequestAddDto("Item1", "someDescription", true, 1L, 1L);
        itemResponse = new ItemDto(1,
                "Item1", "someDescription",
                new UserDTO(1L, "Jo", "tt@rt.ru"),
                true,
                null,
                null,
                List.of(),
                null);
        commentDto = new CommentDto(1L,
                "LOREM IPSUM",
                "SAD",
                LocalDateTime.now());
    }

    @Test
    void checkValidItem() throws Exception {
        Mockito.when(itemClient.create(any(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(itemResponse));
        mockMvc
                .perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value(itemResponse.getName()))
                .andExpect(jsonPath("$.description").value(itemResponse.getDescription()))
                .andExpect(jsonPath("$.owner.id").value(itemResponse.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(itemResponse.getOwner().getName()))
                .andExpect(jsonPath("$.owner.email").value(itemResponse.getOwner().getEmail()))
                .andExpect(jsonPath("$.available").value(itemResponse.getAvailable()))
                .andExpect(jsonPath("$.lastBooking").value(itemResponse.getLastBooking()))
                .andExpect(jsonPath("$.nextBooking").value(itemResponse.getNextBooking()))
                .andExpect(jsonPath("$.comments").isEmpty())
                .andExpect(jsonPath("$.request").value(itemResponse.getRequest()));
    }

    @Test
    void checkItemWithoutName() throws Exception {
        Mockito.when(itemClient.create(any(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(itemResponse));
        item.setName(null);
        mockMvc
                .perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("must not be blank"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkItemWithoutDescription() throws Exception {
        Mockito.when(itemClient.create(any(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(itemResponse));
        item.setDescription(null);
        mockMvc
                .perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("must not be blank"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkItemWithTooLongDescription() throws Exception {
        Mockito.when(itemClient.create(any(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(itemResponse));
        item.setDescription("""
                Exorcizamus te, omnis immundus spiritus, omnis satanica potestas, omnis incursio infernalis adversarii, omnis legio, omnis congregatio et secta diabolica, in nomine et virtute Domini Nostri Jesu + Christi, eradicare et effugare a Dei Ecclesia, ab animabus ad imaginem Dei conditis ac pretioso divini Agni sanguine redemptis + . Non ultra audeas, serpens callidissime, decipere humanum genus, Dei Ecclesiam persequi, ac Dei electos excutere et cribrare sicut triticum + . Imperat tibi Deus altissimus + , cui in magna tua superbia te similem haberi adhuc praesumis; qui omnes homines vult salvos fieri et ad agnitionem veritaris venire. Imperat tibi Deus Pater + ; imperat tibi Deus Filius + ; imperat tibi Deus Spiritus Sanctus + . Imperat tibi majestas Christi, aeternum Dei Verbum, caro factum + , qui pro salute generis nostri tua invidia perditi, humiliavit semetipsum facfus hobediens usque ad mortem; qui Ecclesiam suam aedificavit supra firmam petram, et portas inferi adversus eam nunquam esse praevalituras edixit, cum ea ipse permansurus omnibus diebus usque ad consummationem saeculi. Imperat tibi sacramentum Crucis + , omniumque christianae fidei Mysteriorum virtus +. Imperat tibi excelsa Dei Genitrix Virgo Maria + , quae superbissimum caput tuum a primo instanti immaculatae suae conceptionis in sua humilitate contrivit. Imperat tibi fides sanctorum Apostolorum Petri et Pauli, et ceterorum Apostolorum + . Imperat tibi Martyrum sanguis, ac pia Sanctorum et Sanctarum omnium intercessio +.
                Ergo, draco maledicte et omnis legio diabolica, adjuramus te per Deum + vivum, per Deum + verum, per Deum + sanctum, per Deum qui sic dilexit mundum, ut Filium suum unigenitum daret, ut omnes qui credit in eum non pereat, sed habeat vitam aeternam: cessa decipere humanas creaturas, eisque aeternae perditionis venenum propinare: desine Ecclesiae nocere, et ejus libertati laqueos injicere. Vade, satana, inventor et magister omnis fallaciae, hostis humanae salutis. Da locum Christo, in quo nihil invenisti de operibus tuis; da locum Ecclesiae uni, sanctae, catholicae, et apostolicae, quam Christus ipse acquisivit sanguine suo. Humiliare sub potenti manu Dei; contremisce et effuge, invocato a nobis sancto et terribili nomine Jesu, quem inferi tremunt, cui Virtutes caelorum et Potestates et Dominationes subjectae sunt; quem Cherubim et Seraphim indefessis vocibus laudant, dicentes: Sanctus, Sanctus, Sanctus Dominus Deus Sabaoth.
                V. Domine, exaudi orationem meam.
                R. Et clamor meus ad te veniat.
                [si fuerit saltem diaconus subjungat V. Dominus vobiscum.
                R. Et cum spiritu tuo.]
                Oremus. Deus coeli, Deus terrae, Deus Angelorum, Deus Archangelorum, Deus Patriarcharum, Deus Prophetarum, Deus Apostolorum, Deus Martyrum, Deus Confessorum, Deus Virginum, Deus qui potestatem habes donare vitam post mortem, requiem post laborem; quia non est Deus praeter te, nec esse potest nisi tu creator omnium visibilium et invisibilium, cujus regni non erit finis: humiIiter majestati gloriae tuae supplicamus, ut ab omni infernalium spirituum potestate, laqueo, deceptione et nequitia nos potenter liberare, et incolumes custodire digneris. Per Christum Dominum nostrum. Amen.
                Ab insidiis diaboli, libera nos, Domine.
                Ut Ecclesiam tuam secura tibi facias libertate servire, te rogamus, audi nos.
                Ut inimicos sanctae Ecclesiae humiliare digneris, te rogamus audi nos.
                
                Et aspergatur locus aqua benedicta.
                """);
        mockMvc
                .perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.description").value("size must be between 1 and 255"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkItemWithoutAvailable() throws Exception {
        Mockito.when(itemClient.create(any(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(itemResponse));
        item.setAvailable(null);
        mockMvc
                .perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.available").value("must not be null"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkAddComment() throws Exception {
        Mockito.when(itemClient.addComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
        item.setAvailable(null);
        mockMvc
                .perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(new AddCommentDto("SAD")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(commentDto.getId()))
                .andExpect(jsonPath("$.text").value(commentDto.getText()))
                .andExpect(jsonPath("$.authorName").value(commentDto.getAuthorName()))
                .andExpect(jsonPath("$.created").value(commentDto.getCreated().format(formatter)));
    }

    @Test
    void checkAddCommentWithoutText() throws Exception {
        Mockito.when(itemClient.addComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
        item.setAvailable(null);
        mockMvc
                .perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(new AddCommentDto(null)))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.text").value("must not be blank"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkAddCommentWithBlankText() throws Exception {
        Mockito.when(itemClient.addComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
        item.setAvailable(null);
        mockMvc
                .perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(new AddCommentDto("")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.text").value("size must be between 1 and 255"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

    @Test
    void checkAddCommentWithTooLongText() throws Exception {
        Mockito.when(itemClient.addComment(any(), anyLong(), anyLong()))
                .thenReturn(ResponseEntity.status(HttpStatus.CREATED).body(commentDto));
        item.setAvailable(null);
        mockMvc
                .perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(new AddCommentDto("""
                                Задача организации, в особенности же выбранный нами инновационный путь играет важную роль в формировании дальнейших направлений развитая системы массового участия. Равным образом консультация с профессионалами из IT требует от нас анализа существующих финансовых и административных условий. Разнообразный и богатый опыт повышение уровня гражданского сознания позволяет оценить значение новых предложений!
                                
                                Соображения высшего порядка, а также рамки и место обучения кадров влечет за собой процесс внедрения и модернизации системы масштабного изменения ряда параметров. Практический опыт показывает, что постоянное информационно-техническое обеспечение нашей деятельности обеспечивает широкому кругу специалистов участие в формировании существующих финансовых и административных условий? Практический опыт показывает, что рамки и место обучения кадров требует от нас системного анализа дальнейших направлений развитая системы массового участия. Соображения высшего порядка, а также реализация намеченного плана развития влечет за собой процесс внедрения и модернизации модели развития!
                                
                                С другой стороны рамки и место обучения кадров способствует подготовке и реализации соответствующих условий активизации.
                                
                                Дорогие друзья, начало повседневной работы по формированию позиции влечет за собой процесс внедрения и модернизации соответствующих условий активизации? Не следует, однако, забывать о том, что выбранный нами инновационный путь представляет собой интересный эксперимент проверки соответствующих условий активизации. Разнообразный и богатый опыт начало повседневной работы по формированию позиции играет важную роль в формировании ключевых компонентов планируемого...""")))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(USER_HEADER, String.valueOf(1L))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.text").value("size must be between 1 and 255"))
                .andExpect(jsonPath("$.error").value("Ошибка валидации полей"));
    }

}
