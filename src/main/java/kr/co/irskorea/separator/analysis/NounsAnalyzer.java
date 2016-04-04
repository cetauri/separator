package kr.co.irskorea.separator.analysis;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.analysis.ko.morph.AnalysisOutput;
import org.apache.lucene.analysis.ko.morph.MorphAnalyzer;
import org.apache.lucene.analysis.ko.morph.MorphException;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cetauri on 16. 4. 4..
 */
public class NounsAnalyzer {

    public List<String> getNouns(String text) throws IOException {

        KoreanAnalyzer analyzer = new KoreanAnalyzer(Version.LUCENE_46);
        MorphAnalyzer ma = new MorphAnalyzer();

        TokenStream ts = analyzer.tokenStream(null, new StringReader(text));
        CharTermAttribute termAtt = ts.addAttribute(CharTermAttribute.class);

        List<String> list = new ArrayList<>();

        try {
            ts.reset();
            while (ts.incrementToken()) {
                String token = termAtt.toString();
                List<AnalysisOutput> results = ma.analyze(token);

                for(AnalysisOutput o : results){
                    String term = o.getSource();
                    if (term.length() == 1 || !o.toString().endsWith("(N)") || term.length() > 10) {
                        continue;
                    }

                    list.add(term);
                }
            }

            ts.end();

        } catch (java.lang.StringIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (MorphException e) {
            e.printStackTrace();
        } finally {
            ts.close();
        }


        return list;
    }
}
