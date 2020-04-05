package com.henry.as.config;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * let spring to manage lucene objects
 *
 * @author henryxu
 */
@Configuration
public class LuceneConfig {

    // index dir
    private static final String LUCENE_INDEX_PATH = "index";

    @Bean
    public Analyzer analyzer() {
        return new StandardAnalyzer();
    }


    @Bean
    public Directory directory() throws IOException {

        Path path = Paths.get(LUCENE_INDEX_PATH);
        File file = path.toFile();
        if (!file.exists()) {
            file.mkdirs();
        }
        return FSDirectory.open(path);
    }

    @Bean
    public IndexWriter indexWriter(Directory directory, Analyzer analyzer) throws IOException {
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        indexWriter.deleteAll();
        // clean all index files
        indexWriter.commit();
        return indexWriter;
    }

    @Bean
    @Lazy
    public IndexSearcher indexSearcher(Directory directory) throws IOException {
        IndexReader reader = DirectoryReader.open(directory);
        return new IndexSearcher(reader);
    }

}
