package com.czxy.bos.lucene;

import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class LuceneBasicTest01 {

    @Test
    public void testCreateIndex01() throws  Exception{
        //1 创建文档对象
        Document document = new Document();
        //2 创建Field字段
        //对于编号，可以使用DoubleField、IntField、FloatField、StringField...
        //第一个参数：对应mysql中的字段名字
        //第二个参数：该字段的值
        //第三个参数：是否存储
        document.add(new StringField("id","1",Field.Store.YES));
        //对于title或者content等文件较多的内容，可以使用TextField
        document.add(new TextField("title","谷歌地图之父跳槽Facebook",Field.Store.YES));


        //3 创建索引目录对象:文件内容的存储，可以存储在硬盘上，也可以存储在内存中
        // 如果存储在硬盘上，使用FSDirectory
        //          内存中，使用RAMDirectory
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //4 创建分词器
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //5 创建索引写出器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        //6 创建索引写出器对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);
        //7 写出索引，将索引写入索引库
        indexWriter.addDocument(document);
        //8 提交
        indexWriter.commit();
        //9 关闭
        indexWriter.close();

        System.out.println("okok.....");

    }

    /**
     * 测试存储
     * @throws Exception
     */
    @Test
    public void testCreateIndex02() throws  Exception{
        //1 创建文档对象
        Document document = new Document();
        //2 创建Field字段
        //对于编号，可以使用DoubleField、IntField、FloatField、StringField...
        //第一个参数：对应mysql中的字段名字
        //第二个参数：该字段的值
        //第三个参数：是否存储
        document.add(new StringField("id","1",Field.Store.YES));
        //对于title或者content等文件较多的内容，可以使用TextField
        document.add(new TextField("title","谷歌地图之父跳槽Facebook",Field.Store.YES));
        // 测试存储
        document.add(new StoredField("content","热烈欢迎谷歌地图之父拉斯加入到了Facebook，可助Facebook一臂之力"));

        //3 创建索引目录对象:文件内容的存储，可以存储在硬盘上，也可以存储在内存中
        // 如果存储在硬盘上，使用FSDirectory
        //          内存中，使用RAMDirectory
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //4 创建分词器
        StandardAnalyzer analyzer = new StandardAnalyzer();
        //5 创建索引写出器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        //6 创建索引写出器对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //7 写出索引，将索引写入索引库
        indexWriter.addDocument(document);
        //8 提交
        indexWriter.commit();
        //9 关闭
        indexWriter.close();

        System.out.println("okok.....");

    }

    /**
     * 测试中文IK分词器
     * @throws Exception
     */
    @Test
    public void testCreateIndex03() throws  Exception{
        //1 创建文档对象
        Document document = new Document();
        //2 创建Field字段
        //对于编号，可以使用DoubleField、IntField、FloatField、StringField...
        //第一个参数：对应mysql中的字段名字
        //第二个参数：该字段的值
        //第三个参数：是否存储
        document.add(new StringField("id","1",Field.Store.YES));
        //对于title或者content等文件较多的内容，可以使用TextField
        document.add(new TextField("title","李开复加入了传智大学，碉堡了，厉害吗",Field.Store.YES));
        // 测试存储
        //document.add(new TextField("content","热烈欢迎谷歌地图之父拉斯加入到了Facebook，可助Facebook一臂之力"));

        //3 创建索引目录对象:文件内容的存储，可以存储在硬盘上，也可以存储在内存中
        // 如果存储在硬盘上，使用FSDirectory
        //          内存中，使用RAMDirectory
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //4 创建分词器
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        // 换成IK分词器
        IKAnalyzer analyzer = new IKAnalyzer();

        //5 创建索引写出器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        //OpenMode.CREATE:清空原来的索引。重新创建
        //OpenMode.APPEND:在原来的基础上追加
        //OpenMode.CREATE_OR_APPEND:不存在，新建；存在，追加
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);


        //6 创建索引写出器对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //7 写出索引，将索引写入索引库
        indexWriter.addDocument(document);
        //8 提交
        indexWriter.commit();
        //9 关闭
        indexWriter.close();

        System.out.println("okok.....");

    }


    /**
     * 批量创建索引
     * @throws Exception
     */
    @Test
    public void testCreateIndex04() throws  Exception{

        Collection<Document> docs = new ArrayList<>();

        //1 创建文档对象
//        Document d1 = new Document();
//        d1.add(new StringField("id","1",Field.Store.YES));
//        d1.add(new TextField("title","谷歌地图之父跳槽Facebook",Field.Store.YES));
//
//
//        Document d2 = new Document();
//        d2.add(new StringField("id","2",Field.Store.YES));
//        d2.add(new TextField("title","谷歌地图之父加盟Facebook",Field.Store.YES));
//
//
//        Document d3 = new Document();
//        d3.add(new StringField("id","3",Field.Store.YES));
//        d3.add(new TextField("title","谷歌地图创始人拉斯离开谷歌加盟Facebook",Field.Store.YES));
//
//
//        Document d4 = new Document();
//        d4.add(new StringField("id","4",Field.Store.YES));
//        d4.add(new TextField("title","谷歌地图之父跳槽Facebook与Wave项目取消有关",Field.Store.YES));
//
//
//        Document d5 = new Document();
//        d5.add(new StringField("id","5",Field.Store.YES));
//        d5.add(new TextField("title","谷歌地图之父拉斯加盟社交网站Facebook",Field.Store.YES));



        Document d1 = new Document();
        d1.add(new LongField("id",1l,Field.Store.YES));
        d1.add(new TextField("title","谷歌地图之父跳槽Facebook",Field.Store.YES));


        Document d2 = new Document();
        d2.add(new LongField("id",2l,Field.Store.YES));
        d2.add(new TextField("title","谷歌地图之父加盟Facebook",Field.Store.YES));


        Document d3 = new Document();
        d3.add(new LongField("id",3l,Field.Store.YES));
        d3.add(new TextField("title","谷歌地图创始人拉斯离开谷歌加盟Facebook",Field.Store.YES));


        Document d4 = new Document();
        d4.add(new LongField("id",4l,Field.Store.YES));
        TextField field = new TextField("title", "谷歌地图之父跳槽Facebook与Wave项目取消有关", Field.Store.YES);
        field.setBoost(10);
        d4.add(field);


        Document d5 = new Document();
        d5.add(new LongField("id",5l,Field.Store.YES));
        d5.add(new TextField("title","谷歌地图之父拉斯加盟社交网站Facebook",Field.Store.YES));


        docs.add(d1);
        docs.add(d2);
        docs.add(d3);
        docs.add(d4);
        docs.add(d5);

        //3 创建索引目录对象:文件内容的存储，可以存储在硬盘上，也可以存储在内存中
        // 如果存储在硬盘上，使用FSDirectory
        //          内存中，使用RAMDirectory
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //4 创建分词器
        //StandardAnalyzer analyzer = new StandardAnalyzer();
        // 换成IK分词器
        IKAnalyzer analyzer = new IKAnalyzer();

        //5 创建索引写出器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST, analyzer);
        //OpenMode.CREATE:清空原来的索引。重新创建
        //OpenMode.APPEND:在原来的基础上追加
        //OpenMode.CREATE_OR_APPEND:不存在，新建；存在，追加
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);


        //6 创建索引写出器对象
        IndexWriter indexWriter = new IndexWriter(directory, indexWriterConfig);

        //7 写出索引，将索引写入索引库
//        indexWriter.addDocument(document);

        indexWriter.addDocuments(docs);


        //8 提交

        indexWriter.commit();
        //9 关闭
        indexWriter.close();

        System.out.println("okok.....");

    }


    /*****************查询******************/
    @Test
    public void testSearch01() throws Exception{
        //1 创建索引目录对象---索引在硬盘，读取
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //2 创建目录读取工具--通过工具读取目录中的内容
        DirectoryReader reader = DirectoryReader.open(directory);
        //3 创建索引搜索工具--通过搜索工具搜索内容
        IndexSearcher searcher = new IndexSearcher(reader);
        //4 创建查询解析器--查询哪一列？这一列有什么特殊要求？
        // 第一个参数：搜索的列名
        // 第二个参数：分词器
//        QueryParser parser = new QueryParser("title",new IKAnalyzer());

        // 查询多列
        MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"id", "title","content"}, new IKAnalyzer());


        //5 创建查询对象--从title中搜索什么内容
        Query query = parser.parse("传智");
        //6 搜索数据--执行查询
        // 第一个参数：查询对象
        // 第二个参数：结果条数
        // 返回值： 只包含两个结果：1 击中数   2 集中的文档编号(此编号是lucene给文档分配的编号)
        TopDocs topDocs = searcher.search(query, 10);
        //7 各种操作--获取/解析  查询结果
        int totalHits = topDocs.totalHits;//命中数   “谷歌”5   “加盟”1
        System.out.println("命中次数/击中次数:"+totalHits);
        //查找出得分最高的10个，把得分最高的几个结果显示在最上面，获取这些数据
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //遍历结果
        for(ScoreDoc sd:scoreDocs){
            //通过sd获取文档编号
            int docID = sd.doc;
            //获取得分

            // 通过文档编号  获取 文档内容;借助读取工具读取文档
            Document doc = reader.document(docID);
            // 返回文档
            System.out.print(doc.get("id")+":");
            System.out.println(doc.get("title")+":"+"得分:"+sd.score);

        }
    }


    /**
     * 查询的公共方法
     * @param query
     * @throws Exception
     */
    public void search(Query query) throws Exception{
        // 1 创建目录对象--物理空间位置
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        // 2 创建目录读取工具
        DirectoryReader reader = DirectoryReader.open(directory);
        // 3 创建索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        // 4 创建查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        // 5 创建查询
        // Query query = parser.parse("谷歌");



        // 6 查询
        TopDocs topDocs = searcher.search(query, 10);
        // 7 解析结果
        System.out.println("击中数:"+topDocs.totalHits);
        // 获取返回的文档编号数组
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(ScoreDoc sd:scoreDocs){
            // 获取文档编号
            int docID = sd.doc;
            // 通过docId获取文档信息
            Document doc = reader.document(docID);

            System.out.println("id:"+doc.get("id")+";title:"+doc.get("title")+"；得分:"+sd.score);

        }


    }

    /**
     * TermQuery
     * 词条查询：被查询的词条是最小单位，不可再分词
     * @throws Exception
     */
    @Test
    public void testTermQuery() throws Exception{
//        词语、词条
        Term term = new Term("title", "加盟");
        TermQuery query = new TermQuery(term);

        search(query);

    }


    /**
     * 比较特殊的查询：
     * 可以通过通配符比较  ？：一个字符位置
     *                   * : 任意多个字符
     * @throws Exception
     */
    @Test
    public void testWildcardQuery() throws Exception{
        /**
         *new Term("title", "歌")---------0
         *new Term("title", "?歌")--------5
         * new Term("title", "歌*")-------0
         * new Term("title", "*歌*")------5
         * new Term("title", "?歌*")------5
         */
        WildcardQuery query = new WildcardQuery(new Term("title", "*歌*"));

        search(query);
    }



    @Test
    public void testFuzzyQuery() throws Exception{
        /**
         * FuzzyQuery：模糊查找
         * new Term("title", "Facebook")------5个
         * new Term("title", "Facebooo")------5个
         * new Term("title", "Facabooo")------5个
         * new Term("title", "Facooooo")------0个
         * 允许错误的次数在0-2之间，
         *
         *
         *
         */
        //                                                     facebook
        //FuzzyQuery query = new FuzzyQuery(new Term("title", "facooooo"));

        // 指定修改的次数
        Term term = new Term("title", "facebook");
        FuzzyQuery query = new FuzzyQuery(term, 2);

        search(query);

    }

    /**
     * 范围查找
     */
    @Test
    public void testNumricRangeQuery() throws Exception{
        // 第一个参数：查询的列/字段
        //   2      ：最小值
        //   3      ：最大值
        //   4      ：是否包含最小值 ，true：包含；false:不包含
        //   5      ：是否包含最大值
        NumericRangeQuery<Long> query = NumericRangeQuery.newLongRange("id", 1l, 3l, true, true);

        search(query);
    }

    /**
     * boolean跟上述的查询都不一样，他只是用来组装查询条件，还是需要借助上述的查询方式
     *
     *  if(!(a==b&&c==l)){
     *
     *  }
     *
     * @throws Exception
     */
    @Test
    public void testBooleanQuery()  throws Exception{

        NumericRangeQuery<Long> q1 = NumericRangeQuery.newLongRange("id", 1l, 3l, true, true);
        NumericRangeQuery<Long> q2 = NumericRangeQuery.newLongRange("id", 2l, 4l, true, true);

        BooleanQuery query = new BooleanQuery();

        // 组合1:1,2,3
//        query.add(q1,BooleanClause.Occur.MUST);// 必须要满足q1中的要求
//        query.add(q2,BooleanClause.Occur.SHOULD);//或者满足q2中的要求

        //组合2：1,2,3,4 ： 并集
        query.add(q1,BooleanClause.Occur.SHOULD);
        query.add(q2,BooleanClause.Occur.SHOULD);

        // 组合3：2,3    ：  交集
//        query.add(q1,BooleanClause.Occur.MUST);
//        query.add(q2,BooleanClause.Occur.MUST);




        search(query);

    }


    @Test
    public void testUpdateIndex() throws  Exception{
        /**
         * 1、  lucene没有提供更新数据的操作，
         *    它的更新其实是：底层先删除在插入
         * 2、 通过词条匹配，那么所有匹配词条的数据都会被删除，好吗？
         * 3、 在更新的时候，最好通过唯一确认的那个字段来执行词条查找
         * update
         */
        //1 创建文档存储目录
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //2 创建索引写入器配置对象
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LATEST,new IKAnalyzer());
        //3 创建索引写入器
        IndexWriter indexWriter = new IndexWriter(directory,indexWriterConfig);
        //4 创建文档数据
        Document doc = new Document();
        doc.add(new StringField("id","1",Field.Store.YES));
        doc.add(new TextField("title","李开复加入了传智大学facebook",Field.Store.YES));
        //5 修改
        indexWriter.updateDocument(new Term("id","1"),doc);
        //6 提交
        indexWriter.commit();
        //7 关闭
        indexWriter.close();

    }


    @Test
    public void testDelete() throws  Exception{

        //1 创建文档对象目录
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //2 创建索引写入器配置对象
        IndexWriterConfig config = new IndexWriterConfig(Version.LATEST, new IKAnalyzer());
        //3 创建索引写入器
        IndexWriter indexWriter = new IndexWriter(directory, config);
        //4 删除
        //4.1 删除的是多个，只要title带有李开复就会被删除
        indexWriter.deleteDocuments(new Term("title","李开复"));

        //4.2  删除查询出来的结果
//        TermQuery query = new TermQuery(new Term("title", "李开复"));
//        indexWriter.deleteDocuments(query);

        //4.3 删除所有
//        indexWriter.deleteAll();

        //5 提交
        indexWriter.commit();
        //6 关闭
        indexWriter.close();



    }

    @Test
    public void testHightLighter() throws Exception{
        //1 创建目录 对象
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //2 创建目录读取工具
        DirectoryReader reader = DirectoryReader.open(directory);
        //3 创建索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        //4 创建查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        //5 创建查询对象
        Query query = parser.parse("谷歌");
        //6 创建格式化器
        // 创建格式化器的时候需要指定前缀后缀
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<em>","</em>");
        //7 创建查询分数工具
        QueryScorer scorer = new QueryScorer(query);
        //8 准备高亮工具
        Highlighter highlighter = new Highlighter(formatter,scorer);
        //9 搜索
        TopDocs topDocs = searcher.search(query, 10);
        //10 获取结果
        int totalHits = topDocs.totalHits;
        System.out.println("击中数:"+totalHits);
        // 获取结果
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //11 用高亮工具处理普通的查询结果
        for(ScoreDoc sd:scoreDocs){
            int docID = sd.doc;
            Document doc = reader.document(docID);
            System.out.println("id:"+doc.get("id")+";title:"+doc.get("title"));

            // 采用高亮工具高亮显示结果
            String title = doc.get("title");
            // 第一个参数：分词器
            // 第二个参数：列
            // 第三个参数：格式化的值
            String htitle = highlighter.getBestFragment(new IKAnalyzer(), "title", title);

            System.out.println("高亮之后的结果:"+htitle);
        }

    }

    /**
     * 排序的列的类型一定是数值类型
     *
     *
     */
    @Test
    public void testOrder() throws  Exception{
        // 创建目录对象
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        // 创建目录读取工具
        DirectoryReader reader = DirectoryReader.open(directory);
        // 创建索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        // 创建查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        // 创建查询对象
        Query query = parser.parse("谷歌");
        // 创建排序对象Sort,需要排序字段SortField
        // 第一个参数：排序的列名
        // 第二个参数：列的类型
        // 第三个参数reverse：升序or降序       true:降序    false：升序
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG, true));
        // 执行搜索
        TopFieldDocs topFieldDocs = searcher.search(query, 10, sort);

        // 解析结果
        ScoreDoc[] scoreDocs = topFieldDocs.scoreDocs;
        //高亮
        for(ScoreDoc sd:scoreDocs){
            int docID = sd.doc;
            // 读取文档
            Document doc = reader.document(docID);
            // 打印输出
            System.out.println("id:"+doc.get("id")+":title:"+doc.get("title"));

        }

    }

    /**
     * lucene并没有提供真正的分页， 所以分页得通过逻辑控制
     *
     *
     * @throws Exception
     */
    @Test
    public void testPage() throws Exception{
        //定义分页的变量
        int pageNum =2; // 第几页的数据
        int pageSize=2;// 每页条数
//        开始索引
        int start = (pageNum-1)*pageSize;
        // 结束索引
        int end = start + pageSize;


        //1 创建目录对象
        FSDirectory directory = FSDirectory.open(new File("d:\\indexDir"));
        //2 创建目录读取工具
        DirectoryReader reader = DirectoryReader.open(directory);
        //3 创建索引搜索工具
        IndexSearcher searcher = new IndexSearcher(reader);
        //4 创建查询解析器
        QueryParser parser = new QueryParser("title", new IKAnalyzer());
        //5 创建查询对象
        Query query = parser.parse("谷歌");

        // 排序对象
        Sort sort = new Sort(new SortField("id", SortField.Type.LONG, false));

        //6 查询0----end条数据     一页10条数据       总共1000条数据    获取第10页的90-100
        TopDocs topDocs = searcher.search(query, end,sort);
        //7 解析结果
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        for(int i=start;i<end;i++){
            ScoreDoc sd = scoreDocs[i];
            int docID = sd.doc;
            Document doc = reader.document(docID);

            // 打印
            System.out.println("id:"+doc.get("id")+":title:"+doc.get("title"));

        }

    }



}
