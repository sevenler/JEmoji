#!/usr/bin/python

import os
import sys

RELEASE_DIR = 'release'

manifest = 'AndroidManifest.xml'
manifest_bk = 'AndroidManifest.xml.bk'
RELEASE_APK = 'jemoji-release.apk'


channels = [i.strip() for i in open(sys.argv[-1]).read().split() if i.strip()]
# channels = ['goapk']

def backup():
    os.system('cp %s %s' % (manifest, manifest_bk))

def restore():
    os.system('rm %s' % manifest)
    os.system('mv %s %s' % (manifest_bk, manifest))

def replace_channel(channel_name):
    from xml.dom.minidom import parse
    dom = parse(open(manifest))

    for i in dom.getElementsByTagName('meta-data'):
        if i.getAttribute('android:name') == 'UMENG_CHANNEL':
            i.setAttribute('android:value', channel_name)

    open(manifest, 'w').write(dom.toxml().encode('utf-8'))

def rename_apk(channel_name):
    apk_file = 'bin/%s' % RELEASE_APK
    channel_apk_file = '%s/%s' % (RELEASE_DIR, '%s-%s' % (channel_name, RELEASE_APK))

    os.system('mv %s %s' % (apk_file, channel_apk_file))

def main():
    if not os.path.exists(RELEASE_DIR):
        os.mkdir(RELEASE_DIR)

    backup()
    os.system('rm %s/*' % RELEASE_DIR)

    for i in channels:
        os.system('ant clean')
        replace_channel(i)
        os.system('ant release')
        rename_apk(i)

    restore()


if __name__ == '__main__':
    if not sys.argv[-1].endswith('txt'):
        print './release.py channels.txt'
        exit(1)

    main()

