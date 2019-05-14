package com.e_commerce.miscroservice.guanzhao_proj;

import com.e_commerce.miscroservice.commons.util.colligate.DateUtil;
import com.e_commerce.miscroservice.commons.util.colligate.StringUtil;

public class test {
   private String PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA5m2jCN5mFMNF7BimWzENLMlm26TqIiLxkw7VvPDDM+aKxrx08NmzSPvyHK/Y6MnwM3kJzJidP0b9xm/fW5YMDpOqGZ84xRJLGo1Ejp+AxHcu40O6LG5JAG/PLD+0/74LIYOTL/HdbZ4Qq7oagqY3btTmLpte/3ffloOFVvcqTi6Gv8FfyjeIoYbGEb+rz6tGn7tVIhOB4CEoPj7xN+WxPyUQFSq3klltHIbjOm8oQxEMaXTUXM6deRHaY818L87r6taXW+dHd4M3OSVHwvyxkAX3gPJ9f+lK8xsu3NGWLt43u8RLFyF1SYRNgiNpBvv+1ZEBnPaPyqExYi8mpiAhAQIDAQAB";
   private String PRIVATE_KEY="MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQDmbaMI3mYUw0Xs\n" +
           "GKZbMQ0syWbbpOoiIvGTDtW88MMz5orGvHTw2bNI+/Icr9joyfAzeQnMmJ0/Rv3G\n" +
           "b99blgwOk6oZnzjFEksajUSOn4DEdy7jQ7osbkkAb88sP7T/vgshg5Mv8d1tnhCr\n" +
           "uhqCpjdu1OYum17/d9+Wg4VW9ypOLoa/wV/KN4ihhsYRv6vPq0afu1UiE4HgISg+\n" +
           "PvE35bE/JRAVKreSWW0chuM6byhDEQxpdNRczp15EdpjzXwvzuvq1pdb50d3gzc5\n" +
           "JUfC/LGQBfeA8n1/6UrzGy7c0ZYu3je7xEsXIXVJhE2CI2kG+/7VkQGc9o/KoTFi\n" +
           "LyamICEBAgMBAAECggEAVPbWJ74Be+Ro0t5f8L4Mcpgsog6T+lRSKPxOWWWZRHjc\n" +
           "cN0qbWiIT//+TQ+V9ngHZqNZtqQLTzq49kNn41hXR8Sw8tOf1iKhqL+wKLqte0PK\n" +
           "+SeO7TUZG90eUd2HusjzR79BNWuETxf6Y6eygLwTMn20Bjlp9N7ZhaEnOGLYqYyQ\n" +
           "/zbWxS9m/UYC/gMU5mCsvA8UzeGWpysBBs8tNaVcjJfnAZs3a57o8JBB7ys2bv+9\n" +
           "n9cEnoyNmbeUdg6meuLFCnfsW/51JYkjsm+88Vbfb210QJ22WF1k0GxitayyitQR\n" +
           "r2KOEVzW8c6N4XbYzEkuAWf66a2aEswQfyDFE55WpQKBgQD95MWtEVO8FZ7Qw4BE\n" +
           "Tdjlt+uTK254Ue2ihzmTHj7RsMGCOi7vZCUg4EvU9iTi+wm0HfkOOVWOSytVTBnh\n" +
           "WcziuO0Cz/HxHpT10QT33Nr1G4abxFaL44MlraHGYgM/T3oMrHlD9ljoPAxtYRxd\n" +
           "YoIX95aD3cu2m8PIF/vCOC4LkwKBgQDoVwc0BQwNwUcerKCaixiNCBp/pX3jL+t2\n" +
           "BE+5umhARI5AY014gYgpyiovYOnBIbLHP59HltaBhAQNuaCNKgvTTTaOj+P+Ywi/\n" +
           "iprDIJR1jB93YsuFZ5ojYzrvU/b24wzenB1HZCEfbm2n4CSnCAxAp3vkOXpz/I2l\n" +
           "runbu63FmwKBgQCyPMBpVxipmaP8esK5MM1rt0ox7vOGlD13M+c/WKRRIP04JxB8\n" +
           "DpniEpQp1QLgNcwCHmiRi758PlkVcG9avGgRYjFBICqBUy7PwwTx3KH6IHDHOXZo\n" +
           "jjerY9GGK5iMAYd1Dj+zbmXL4xv2TY5UIKujl7SarkIs1/0Bo5SiLD5m8QKBgQCt\n" +
           "THSn/Xe/LsIQhCyK+WNHlprFSTg74/taHN8mpn6qKTh9mFQppVxd51hN3JieoLcL\n" +
           "3U2+BSlck46+eg3iHVGSu04NShCCJ3ZYAFdtYfi6MNkroHozuFIFQTetWdCk2Dg6\n" +
           "NvGz1WDLY//QruRH3j3dX8+OLL1ElEVHJs9yQWvdLwKBgQDCmpl4I/aJNVEnSGeG\n" +
           "CT2GkfZeCSC40kMMVrVZAZWSmBUmNn3L3rI/HYoZRU6R74Oz6ceYU4rRHjnIEseG\n" +
           "9GQowZUOZsF3FIOnl/lKDOEk87Faog0omZlj02kM2n6cw/9gjfZB7L6H/YIV3Pgu\n" +
           "ZeZ+7BO6TUIlGBPAMjiSB0W23A==";


   private String url="https://devapi.xiaoshitimebank.com/user/pay/alipaynotify";

}
