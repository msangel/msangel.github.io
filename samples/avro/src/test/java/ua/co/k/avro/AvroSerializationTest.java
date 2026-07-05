package ua.co.k.avro;

import static net.javacrumbs.jsonunit.JsonMatchers.jsonEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.Schema;
import org.apache.avro.SchemaBuilder;
import org.apache.avro.SchemaParser;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.apache.avro.reflect.ReflectDatumReader;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.junit.jupiter.api.Test;
import ua.co.k.avro.generated.User;

@Slf4j
public class AvroSerializationTest {

    @Test
    public void testWritingGenerated() throws IOException {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        User user2 = new User("Ben", 7, "red");

        File file = new File("records_users.avro");

        // Serialize user1, user2 and user3 to disk
        DatumWriter<User> userDatumWriter = new SpecificDatumWriter<>(User.class);
        try (DataFileWriter<User> dataFileWriter = new DataFileWriter<>(userDatumWriter)) {
            dataFileWriter.create(user1.getSchema(), file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
        }

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testWritingCustom() throws IOException {
        CustomUser user1 = new CustomUser();
        user1.setName("Alyssa");
        user1.setFavoriteNumber(256);

        CustomUser user2 = new CustomUser("Ben", 7, "red");

        File file = new File("pojo_users.avro");

        // Serialize user1, user2 and user3 to disk
        DatumWriter<CustomUser> userDatumWriter = new ReflectDatumWriter<>(CustomUser.class);
        try (DataFileWriter<CustomUser> dataFileWriter = new DataFileWriter<>(userDatumWriter)) {
            Schema schema = ReflectData.get().getSchema(CustomUser.class);
            dataFileWriter.create(schema, file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
        }

        assertTrue(file.exists());
        assertTrue(file.length() > 0);
    }

    @Test
    public void testWritingGenericRecord() throws IOException {
        File schemaFile = new File("src/main/resources/avro/user.avsc");
        Schema schema = new SchemaParser().parse(schemaFile).mainSchema();

        GenericRecord user1 = new GenericData.Record(schema);
        user1.put("name", "Alyssa");
        user1.put("favorite_number", 256);

        GenericRecord user2 = new GenericData.Record(schema);
        user2.put("name", "Ben");
        user2.put("favorite_number", 7);
        user2.put("favorite_color", "red");

        File file = new File("record_users.avro");
        DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
        try (DataFileWriter<GenericRecord> dataFileWriter = new DataFileWriter<>(datumWriter)) {
            dataFileWriter.create(schema, file);
            dataFileWriter.append(user1);
            dataFileWriter.append(user2);
        }
    }

    @Test
    public void testReadingGenerated() throws IOException {
        File file = new File("records_users.avro");

        DatumReader<User> userDatumReader = new SpecificDatumReader<>(User.class);
        try (DataFileReader<User> dataFileReader = new DataFileReader<>(file,
                userDatumReader)) {
            User user = null;
            while (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
                log.info("{}", user);
            }
        }
    }

    @Test
    public void testReadingCustom() throws IOException {
        File file = new File("pojo_users.avro");

        DatumReader<CustomUser> userDatumReader = new ReflectDatumReader<>(CustomUser.class);
        try (DataFileReader<CustomUser> dataFileReader = new DataFileReader<>(file,
                userDatumReader)) {
            CustomUser user = null;
            while (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
                log.info("{}", user);
            }
        }
    }

    @Test
    public void testReadingGenericRecord() throws IOException {
        File schemaFile = new File("src/main/resources/avro/user.avsc");
        Schema schema = new SchemaParser().parse(schemaFile).mainSchema();

        File file = new File("record_users.avro");
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
        try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)) {
            GenericRecord user = null;
            while (dataFileReader.hasNext()) {
                user = dataFileReader.next(user);
                log.info("{}", user);
            }
        }
    }

    @Test
    public void testSchemaBuilder() throws IOException {
        Schema schema = SchemaBuilder.record("User")
                .namespace("ua.co.k.avro.generated")
                .fields()
                .name("name").type().stringType().noDefault()
                .name("favorite_number").type().nullable().intType().noDefault()
                .name("favorite_color").type().nullable().stringType().noDefault()
                .endRecord();

        assertThat(schema.toString(), jsonEquals("{\n"
                + "  \"namespace\": \"ua.co.k.avro.generated\",\n"
                + "  \"type\": \"record\",\n"
                + "  \"name\": \"User\",\n"
                + "  \"fields\": [\n"
                + "    { \"name\": \"name\", \"type\": \"string\" },\n"
                + "    { \"name\": \"favorite_number\",  \"type\": [\"int\", \"null\"] },\n"
                + "    { \"name\": \"favorite_color\", \"type\": [\"string\", \"null\"] }\n"
                + "  ]\n"
                + "}\n"));
    }
}
