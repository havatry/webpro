// ���ݷ��ؽ������domԪ��
function taskQueueUpdate(tableDom, data) {
    /**
     * {"data":[{"algorithm":"algorithm","date":"2020-03-07 23:52:01","id":74,"status":"��ִ����","type":"�ֶ�����"},{"algorithm":"algorithm","date":"2020-03-07 21:08:25","id":73,"status":"ӳ��ɹ�","type":"�ֶ�����"},{"algorithm":"algorithm","date":"2020-03-07 21:07:57","id":72,"status":"ӳ��ɹ�","type":"�ֶ�����"},{"algorithm":"algorithm","date":"2020-03-07 21:07:25","id":71,"status":"ӳ��ɹ�","type":"�ֶ�����"},{"algorithm":"algorithm","date":"2020-03-07 20:54:58","id":70,"status":"ӳ��ɹ�","type":"�ֶ�����"},{"algorithm":"algorithm","date":"2020-03-07 20:54:34","id":69,"status":"ӳ��ɹ�","type":"�ֶ�����"}],"total":6}
     */
    var tab = '';
    var header = '<tr style="color: rgb(209, 73, 78);"><td>ʱ��</td><td>����</td>' +
        '<td>״̬</td><td>���</td><td>����</td></tr>';
    // �������
    var content = data['data'];
    tab += header;
    for (var i = 0; i < content.length; i++) {
        var line = templateTr().format(content[i].date, content[i].type, statusLabel(content[i].status), content.status, )
    }
}

function templateTr() {
    return '<tr><td>{0}</td><td>{1}</td><td><span class="{2}">{3}</span></td><td><button class="{4}" style="color: black" {5}>�鿴</button>' +
        '<button class="{6}" style="color: black">����</button></td><td><button class="btn btn-danger">ɾ��</button><button class="btn btn-success">ִ��</button></td></tr>';
}

function statusLabel(status) {
    switch (status) {
        case '��ִ����':
            return 'label label-default';
        case 'ִ�����':
            return 'label label-primary';
        case 'ӳ��ɹ�':
            return 'label label-success';
        default:
            return 'label label-danger';
    }
}

String.prototype.format = function() {
    if (arguments.length == 0) return this;
    var param = arguments[0];
    var s = this;
    if (typeof(param) == 'object') {
        for (var key in param)
            s = s.replace(new RegExp("\\{" + key + "\\}", "g"), param[key]);
        return s;
    } else {
        for (var i = 0; i < arguments.length; i++)
            s = s.replace(new RegExp("\\{" + i + "\\}", "g"), arguments[i]);
        return s;
    }
}