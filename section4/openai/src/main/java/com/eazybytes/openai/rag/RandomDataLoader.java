package com.eazybytes.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.eazybytes.openai.rag.BasicCodeChunker.chunkSource;

//@Component
public class RandomDataLoader {

    private final VectorStore vectorStore;

    public RandomDataLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadSentencesIntoVectorStore() {
        List<BasicCodeChunker.CodeChunk> codeChunkList = chunkSource(
                "     SUBROUTINE IWKTARIFF\n" +
                        "*--------------------------------------------------------------------\n" +
                        "* Copyright     | Sanderson Computers Pty Ltd\n" +
                        "*               | All rights reserved.\n" +
                        "* Author        | IWK Training\n" +
                        "* Date Written  | 15 FEB 2004\n" +
                        "* Last Mod Date |\n" +
                        "*--------------------------------------------------------------------\n" +
                        "* Standard program banner\n" +
                        "*--------------------------------------------------------------------\n" +
                        "*\n" +
                        "     INCLUDE GALA.EQUS GA.COMMONS\n" +
                        "     COMMON GLOBAL.EXTRAS(50),EXTRAS(50)\n" +
                        "     INCLUDE GALA.EQUS GASUB.PARAMS\n" +
                        "*\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.DATA\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.FLD\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.FILE\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.REC\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.KEY\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.FN\n" +
                        "     INCLUDE IWK.SCREEN.DEFS IWKTARIFF.WIN\n" +
                        "*\n" +
                        "     INCLUDE GALA.EQUS GA.STD.EQUS\n" +
                        "     INCLUDE GALA.EQUS ACT.CODES\n" +
                        "     INCLUDE GALA.EQUS SAVE.VARIABLES\n" +
                        "     INCLUDE GALA.EQUS SETFG$VALIDATE\n" +
                        "*\n" +
                        "     BEGIN CASE\n" +
                        "        CASE FIRST.TIME\n" +
                        "        CASE FG$UPDATE\n" +
                        "           IF EFFDATE THEN\n" +
                        "              LOCATE EFFDATE IN EFFDATE.FIELD<1>,1 BY 'AR' SETTING POS ELSE\n" +
                        "                 INS EFFDATE BEFORE EFFDATE.FIELD<1,POS>\n" +
                        "                 INS '' BEFORE RANGE.FIELD<1,POS>\n" +
                        "                 INS '' BEFORE RATE.FIELD<1,POS>\n" +
                        "              END\n" +
                        "              DC=DCOUNT(RANGES,@VM)\n" +
                        "              IF DC=0 THEN\n" +
                        "                 DEL EFFDATE.FIELD<1,POS>\n" +
                        "                 DEL RANGE.FIELD<1,POS>\n" +
                        "                 DEL RATE.FIELD<1,POS>\n" +
                        "              END ELSE\n" +
                        "                 RANGE.FIELD<1,POS>=''\n" +
                        "                 RATE.FIELD<1,POS>=''\n" +
                        "                 FOR I=1 TO DC\n" +
                        "                    LOCATE ICONV(RANGES<1,I>,'MD2') IN RANGE.FIELD<1,POS>,1 BY 'AR' SETTING SPOS ELSE\n" +
                        "                       INS ICONV(RANGES<1,I>,'MD2') BEFORE RANGE.FIELD<1,POS,SPOS>\n" +
                        "                       INS ICONV(RATES<1,I>,'MD4') BEFORE RATE.FIELD<1,POS,SPOS>\n" +
                        "                    END\n" +
                        "                 NEXT I\n" +
                        "              END\n" +
                        "           END\n" +
                        "        CASE FG$ABORTED\n" +
                        "        CASE FG$FINISHED\n" +
                        "        CASE FG$AMEND AND FG$FLD='ACT'\n" +
                        "        CASE FG$SELECTING AND FG$FLD # ''\n" +
                        "*       Select key check one\n" +
                        "        CASE FG$SELECTING AND FG$FLD = ''\n" +
                        "*       Select key check two\n" +
                        "        CASE FG$DELETING\n" +
                        "*       Delete paging line\n" +
                        "        CASE FG$FLD=EFFDATE.FLD AND FG$VALIDATE\n" +
                        "           IF EFFDATE THEN\n" +
                        "              DAY=OCONV(EFFDATE,'DD')\n" +
                        "              IF DAY#1 THEN\n" +
                        "                 CALL GALA.ERRMSG('Tariff Effective Dates Must be from 1st of Month')\n" +
                        "                 VALID=0\n" +
                        "                 GOTO 9999\n" +
                        "              END\n" +
                        "              RANGES=''\n" +
                        "              RATES=''\n" +
                        "              DUP=''\n" +
                        "              FG$NBR.PANES=''\n" +
                        "              LOCATE EFFDATE IN EFFDATE.FIELD<1>,1 SETTING POS THEN\n" +
                        "                 DC=DCOUNT(RANGE.FIELD<1,POS>,@SVM)\n" +
                        "                 FOR I=1 TO DC\n" +
                        "                    RANGES<1,I>=OCONV(RANGE.FIELD<1,POS,I>,'MD2')\n" +
                        "                    RATES<1,I>=OCONV(RATE.FIELD<1,POS,I>,'MD4')\n" +
                        "                 NEXT I\n" +
                        "                 FG$NBR.PANES=DC\n" +
                        "                 FG$AMEND=TRUE\n" +
                        "                 FG$NEXT.FLD=LAST.FIELD.FLD\n" +
                        "              END\n" +
                        "              FG$REFRESH=FG$FULL\n" +
                        "              CALL GALA.REFRESH\n" +
                        "           END\n" +
                        "        CASE FG$FLD=DUP.FLD\n" +
                        "           IF DUP#'' THEN\n" +
                        "              READ REC FROM F.TARIFF.CODES,DUP ELSE REC=''\n" +
                        "              LOCATE EFFDATE IN REC<2>,1 SETTING POS ELSE\n" +
                        "                 CALL GALA.ERRMSG('Effective date not setup on code ':DUP)\n" +
                        "                 VALID=0\n" +
                        "              END\n" +
                        "              IF VALID THEN\n" +
                        "                 DC=DCOUNT(REC<3,POS>,@SVM)\n" +
                        "                 FOR I=1 TO DC\n" +
                        "                    RANGES<1,I>=OCONV(REC<3,POS,I>,'MD2')\n" +
                        "                    RATES<1,I>=OCONV(REC<4,POS,I>,'MD4')\n" +
                        "                 NEXT I\n" +
                        "                 FG$NBR.PANES=DC\n" +
                        "                 FG$AMEND=TRUE\n" +
                        "                 FG$NEXT.FLD=LAST.FIELD.FLD\n" +
                        "                 FG$REFRESH=FG$FULL\n" +
                        "                 CALL GALA.REFRESH\n" +
                        "              END\n" +
                        "           END\n" +
                        "        CASE FG$FLD=GROUP.FLD AND FG$VALIDATE\n" +
                        "           IF CODE MATCH '2N' ELSE\n" +
                        "              IF GROUP='' THEN\n" +
                        "                 CALL GALA.ERRMSG('Must specify rate group for this tariff')\n" +
                        "                 VALID=0\n" +
                        "              END\n" +
                        "           END\n" +
                        "        CASE FG$FLD=LAST.FIELD.FLD\n" +
                        "        CASE 1\n" +
                        "           MSG='Warning: No code for field ':FG$FLD:' validation type ':FG$TYPECODE\n" +
                        "           CALL GALA.PERRMSG(BELL,MSG,'',RESP,'OK')\n" +
                        "     END CASE\n" +
                        "     GO 9999\n" +
                        "*\n" +
                        "*--------------------------------------------------------------------\n" +
                        "*\n" +
                        "9999 * Program exit\n" +
                        "*\n" +
                        "     RETURN\n" +
                        "  END\n"
        );
        List<String> sentences = codeChunkList.stream()
                .map(chunk -> chunk.content)
                .filter(content -> content != null && !content.isBlank())
                .toList(); // Java 16+
        List<Document> documents = sentences.stream().map(Document::new).collect(Collectors.toUnmodifiableList());
        vectorStore.add(documents);
    }
}
