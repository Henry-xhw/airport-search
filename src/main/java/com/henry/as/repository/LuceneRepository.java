package com.henry.as.repository;

import com.henry.as.dto.AirLineAndRouteDto;
import com.henry.as.dto.AirPortDto;
import com.henry.as.dto.Query;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * To refactor:
 * 1. can split the class to create index part and search part
 * 2. can move some domain to dto codes into Utils class
 */
@Repository
@Slf4j
public class LuceneRepository {

    private static final String DOCS_PATH = "data";

    @Autowired
    private IndexWriter indexWriter;

    @Lazy
    @Autowired
    private IndexSearcher indexSearcher;

    @Autowired
    private Analyzer analyzer;

    public void createIndex() {
        final Path docDir = Paths.get(DOCS_PATH);
        try {
            indexDocs(indexWriter, docDir);
            // refresh the index file
            indexWriter.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void indexDocs(final IndexWriter writer, Path path) throws IOException {
        if (Files.isDirectory(path)) {
            //Iterate directory
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        //Index this file
                        indexDoc(writer, file);
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            //Index this file
            indexDoc(writer, path);
        }
    }

    private void indexDoc(IndexWriter writer, Path file) throws IOException {
        List<Document> docs = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            // can use simple factory pattern to refactor these codes
            if (file.endsWith("airports.dat")) {
                createAirPortDocuments(docs, reader);
            } else if (file.endsWith("airlines.dat")) {
                createAirLineDocuments(docs, reader);
            } else {
                createRouteDocuments(docs, reader);
            }

            writer.addDocuments(docs);
        }
    }

    private void createAirPortDocuments(List<Document> docs, BufferedReader reader) throws IOException {
        String line = reader.readLine();
        while (line != null) {
            Document doc = new Document();
            String[] lineStrs = StringUtils.split(line, ",");
            doc.add(new StringField("ap_id", lineStrs[0], Field.Store.YES));
            doc.add(new TextField("name", lineStrs[1], Field.Store.YES));
            doc.add(new StringField("city", lineStrs[2], Field.Store.YES));
            doc.add(new StringField("country", lineStrs[3], Field.Store.YES));
            doc.add(new StringField("iata", lineStrs[4], Field.Store.YES));
            doc.add(new StringField("icao", lineStrs[5], Field.Store.YES));
            doc.add(new StringField("latitude", lineStrs[6], Field.Store.YES));
            doc.add(new StringField("longitude", lineStrs[7], Field.Store.YES));
            doc.add(new StringField("altitude", lineStrs[8], Field.Store.YES));
            doc.add(new StringField("timezone", lineStrs[9], Field.Store.YES));
            doc.add(new StringField("dst", lineStrs[10], Field.Store.YES));
            doc.add(new StringField("tz_database_time_zone", lineStrs[11], Field.Store.YES));
            doc.add(new StringField("type", lineStrs[12], Field.Store.YES));
            doc.add(new StringField("source", lineStrs[13], Field.Store.YES));
            docs.add(doc);
            line = reader.readLine();
        }
    }

    private void createAirLineDocuments(List<Document> docs, BufferedReader reader) throws  IOException {
        //Todo
    }

    private void createRouteDocuments(List<Document> docs, BufferedReader reader) throws  IOException {
        //Todo
    }

    public List<AirPortDto> searchAirPort(Query<AirPortDto> query) throws IOException, ParseException {
        List<AirPortDto> apDtos = new ArrayList<>();
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        AirPortDto params = query.getParams();
        Map<String, String> queryParam = query.getQueryParam();
        String keyStr = queryParam.get("searchKeyStr");

        //use 'name' field to fuzzy query
        if (StringUtils.isNotBlank(keyStr)) {
            builder.add(new QueryParser("name", analyzer).parse(keyStr.trim()), BooleanClause.Occur.MUST);
        }
        // use 'iata' field to exact query
        // couldn't exact query successful, why?
        if (params.getIata() != null) {
            builder.add(new TermQuery(new Term("iata", params.getIata())), BooleanClause.Occur.MUST);
        }

        TopDocs foundDocs = indexSearcher.search(builder.build(), 10);
        log.info("Total Results :: " + foundDocs.totalHits);

        for (ScoreDoc sd : foundDocs.scoreDocs) {
            AirPortDto apDto = new AirPortDto();
            Document d = indexSearcher.doc(sd.doc);
            log.info("Score: {}", sd.score);
            // can use lombok's builder pattern to refactor
            apDto.setName(d.get("name"));
            apDto.setId(Integer.parseInt(d.get("ap_id")));
            apDto.setCity(d.get("city"));
            apDto.setCountry(d.get("country"));
            apDto.setIata(d.get("iata"));
            apDto.setIcao(d.get("icao"));
            apDto.setLatitude(Double.valueOf(d.get("latitude")));
            apDto.setLongitude(Double.valueOf(d.get("longitude")));
            apDto.setAltitude(Integer.valueOf(d.get("altitude")));
            apDto.setTimeZone(Float.valueOf(d.get("timezone")));
            apDto.setDst(d.get("dst"));
            apDto.setTzFormat(d.get("tz_database_time_zone"));
            apDto.setType(d.get("type"));
            apDto.setSource(d.get("source"));
            apDtos.add(apDto);
        }
        return apDtos;
    }

    public List<AirLineAndRouteDto> searchAirLineAndRoute(Query<AirLineAndRouteDto> query) throws IOException,
            ParseException {
        //Todo
            return null;
    }


}

