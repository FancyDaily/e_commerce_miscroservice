package com.e_commerce.miscroservice.xiaoshi_proj.product.util;

public class AnsjUtil {
    public final static String n = "名词";
    public final static String nr = "人名";
    public final static String nr1 = "汉语姓氏";
    public final static String nr2 = "汉语名字";
    public final static String nrj = "日语人名";
    public final static String nrf = "音译人名";
    public final static String ns = "地名";
    public final static String nsf = "音译地名";
    public final static String nt = "机构团体名";
    public final static String nz = "其它专名";
    public final static String nl = "名词性惯用语";
    public final static String ng = "名词性语素";
    public final static String nw = "新词";
    public final static String t = "时间词";
    public final static String tg = "时间词";
    public final static String s = "处所词";
    public final static String f = "方位词";
    public final static String v = "动词";
    public final static String vd = "副动词";
    public final static String vn = "名动词";
    public final static String vshi = "动词“是”";
    public final static String vyou = "动词“有”";
    public final static String vf = "趋向动词";
    public final static String vx = "形式动词";
    public final static String vi = "不及物动词（内动词）";
    public final static String vl = "动词性惯用语";
    public final static String vg = "动词性语素";
    public final static String a = "形容词";
    public final static String ad = "副形词";
    public final static String an = "名形词";
    public final static String ag = "形容词性语素";
    public final static String al = "形容词性惯用语";
    public final static String b = "区别词";
    public final static String bl = "区别词性惯用语";
    public final static String z = "状态词";
    public final static String r = "代词";
    public final static String rr = "人称代词";
    public final static String rz = "指示代词";
    public final static String rzt = "时间指示代词";
    public final static String rzs = "处所指示代词";
    public final static String rzv = "代词";
    public final static String ry = "疑问代词";
    public final static String ryt = "时间疑问代词";
    public final static String rys = "处所疑问代词";
    public final static String ryv = "代词性语素";
    public final static String rg = "谓词性疑问代词";
    public final static String m = "数词";
    public final static String mq = "数量词";
    public final static String q = "量词";
    public final static String qv = "动量词";
    public final static String qt = "时量词";
    public final static String d = "副词";
    public final static String p = "介词";
    public final static String pba = "介词“把”";
    public final static String pbei = "介词“被”";
    public final static String c = "连词";
    public final static String cc = "并列连词";
    public final static String u = "助词";
    public final static String uzhe = "着";
    public final static String ule = "了 喽";
    public final static String uguo = "过";
    public final static String ude1 = "的 底";
    public final static String ude2 = "地";
    public final static String ude3 = "得";
    public final static String usuo = "所";
    public final static String udeng = "等 等等 云云";
    public final static String uyy = "一样 一般 似的 般";
    public final static String udh = "的话";
    public final static String uls = "来讲 来说 而言 说来";
    public final static String uzhi = "之";
    public final static String ulian = "连 （“连小学生都会”）";
    public final static String e = "叹词";
    public final static String y = "语气词(delete yg)";
    public final static String o = "叹词";
    public final static String h = "前缀";
    public final static String k = "后缀";
    public final static String x = "字符串";
    public final static String xx = "非语素字";
    public final static String xu = "网址URL";
    public final static String w = "标点符号";
    public final static String wkz = "左括号，全角：（ 〔  ［  ｛  《 【  〖〈   半角：( [ { <";
    public final static String wky = "右括号，全角：） 〕  ］ ｝ 》  】 〗 〉 半角： ) ] { >";
    public final static String wyz = "左引号，全角：“ ‘ 『";
    public final static String wyy = "右引号，全角：” ’ 』";
    public final static String wj = "句号，全角：。";
    public final static String ww = "问号，全角：？ 半角：?";
    public final static String wt = "叹号，全角：！ 半角：!";
    public final static String wd = "逗号，全角：， 半角：,";
    public final static String wf = "分号，全角：； 半角： ;";
    public final static String wn = "顿号，全角：、";
    public final static String wm = "冒号，全角：： 半角： :";
    public final static String ws = "省略号，全角：……  …";
    public final static String wp = "破折号，全角：——   －－   ——－   半角：---  ----";
    public final static String wb = "百分号千分号，全角：％ ‰   半角：%";
    public final static String wh = "单位符号，全角：￥ ＄ ￡  °  ℃  半角：$";
    public final static String nuknown = "未知词性";


    private static String partOfSpeech;

    public static String getPartOfSpeech(String word) {
        switch (word) {
            case "n":
                partOfSpeech = n;
                break;
            case "nr ":
                partOfSpeech = nr;
                break;
            case "nr1":
                partOfSpeech = nr1;
                break;
            case "nr2":
                partOfSpeech = nr2;
                break;
            case "nrj":
                partOfSpeech = nrj;
                break;
            case "nrf":
                partOfSpeech = nrf;
                break;
            case "ns":
                partOfSpeech = ns;
                break;
            case "nsf":
                partOfSpeech = nsf;
                break;
            case "nt":
                partOfSpeech = nt;
                break;
            case "nz":
                partOfSpeech = nz;
                break;
            case "nl":
                partOfSpeech = nl;
                break;
            case "ng":
                partOfSpeech = ng;
                break;
            case "nw":
                partOfSpeech = nw;
                break;
            case "t":
                partOfSpeech = t;
                break;
            case "tg":
                partOfSpeech = tg;
                break;
            case "s":
                partOfSpeech = s;
                break;
            case "f":
                partOfSpeech = f;
                break;
            case "v":
                partOfSpeech = v;
                break;
            case "vd":
                partOfSpeech = vd;
                break;
            case "vn":
                partOfSpeech = vn;
                break;
            case "vshi":
                partOfSpeech = vshi;
                break;
            case "vyou":
                partOfSpeech = vyou;
                break;
            case "vf":
                partOfSpeech = vf;
                break;
            case "vx":
                partOfSpeech = vx;
                break;
            case "vi":
                partOfSpeech = vi;
                break;
            case "vl":
                partOfSpeech = vl;
                break;
            case "vg":
                partOfSpeech = vg;
                break;
            case "a":
                partOfSpeech = a;
                break;
            case "ad":
                partOfSpeech = ad;
                break;
            case "an":
                partOfSpeech = an;
                break;
            case "ag":
                partOfSpeech = ag;
                break;
            case "al":
                partOfSpeech = al;
                break;
            case "b":
                partOfSpeech = b;
                break;
            case "bl":
                partOfSpeech = bl;
                break;
            case "z":
                partOfSpeech = z;
                break;
            case "r":
                partOfSpeech = r;
                break;
            case "rr":
                partOfSpeech = rr;
                break;
            case "rz":
                partOfSpeech = rz;
                break;
            case "rzt":
                partOfSpeech = rzt;
                break;
            case "rzs":
                partOfSpeech = rzs;
                break;
            case "rzv":
                partOfSpeech = rzv;
                break;
            case "ry":
                partOfSpeech = ry;
                break;
            case "ryt":
                partOfSpeech = ryt;
                break;
            case "rys":
                partOfSpeech = rzt;
                break;
            case "ryv ":
                partOfSpeech = ryv;
                break;
            case "rg":
                partOfSpeech = rg;
                break;
            case "m":
                partOfSpeech = m;
                break;
            case "mq":
                partOfSpeech = mq;
                break;
            case "q":
                partOfSpeech = q;
                break;
            case "qv":
                partOfSpeech = qv;
                break;
            case "qt":
                partOfSpeech = qt;
                break;
            case "d":
                partOfSpeech = d;
                break;
            case "p":
                partOfSpeech = p;
                break;
            case "pba":
                partOfSpeech = pba;
                break;
            case "pbei":
                partOfSpeech = pbei;
                break;
            case "c":
                partOfSpeech = c;
                break;
            case "cc":
                partOfSpeech = cc;
                break;
            case "u":
                partOfSpeech = u;
                break;
            case "uzhe":
                partOfSpeech = uzhe;
                break;
            case "ule":
                partOfSpeech = ule;
                break;
            case "uguo":
                partOfSpeech = uguo;
                break;
            case "ude1":
                partOfSpeech = ude1;
                break;
            case "ude2":
                partOfSpeech = ude2;
                break;
            case "ude3":
                partOfSpeech = ude3;
                break;
            case "usuo":
                partOfSpeech = usuo;
                break;
            case "udeng":
                partOfSpeech = udeng;
                break;
            case "uyy":
                partOfSpeech = uyy;
                break;
            case "udh":
                partOfSpeech = udh;
                break;
            case "uls":
                partOfSpeech = uls;
                break;
            case "uzhi":
                partOfSpeech = uzhi;
                break;
            case "ulian":
                partOfSpeech = ulian;
                break;
            case "e":
                partOfSpeech = e;
                break;
            case "y":
                partOfSpeech = y;
                break;
            case "o":
                partOfSpeech = o;
                break;
            case "h":
                partOfSpeech = h;
                break;
            case "k":
                partOfSpeech = k;
                break;
            case "x":
                partOfSpeech = x;
                break;
            case "xx":
                partOfSpeech = xx;
                break;
            case "xu":
                partOfSpeech = xu;
                break;
            case "w":
                partOfSpeech = w;
                break;
            case "wkz":
                partOfSpeech = wkz;
                break;
            case "wky":
                partOfSpeech = wky;
                break;
            case "wyz":
                partOfSpeech = wyz;
                break;
            case "wyy":
                partOfSpeech = wyy;
                break;
            case "wj":
                partOfSpeech = wj;
                break;
            case "ww":
                partOfSpeech = ww;
                break;
            case "wt":
                partOfSpeech = wt;
                break;
            case "wd":
                partOfSpeech = wd;
                break;
            case "wf":
                partOfSpeech = wf;
                break;
            case "wn":
                partOfSpeech = udeng;
                break;
            case "wm":
                partOfSpeech = wm;
                break;
            case "ws":
                partOfSpeech = ws;
                break;
            case "wp":
                partOfSpeech = wp;
                break;
            case "wb":
                partOfSpeech = wb;
                break;
            case "wh":
                partOfSpeech = wh;
                break;
            default:
                partOfSpeech = nuknown;
                break;
        }
        return partOfSpeech;
    }
}
