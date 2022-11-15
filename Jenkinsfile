library('delivery')
library('kkb-k8s-mos-deploy-plugin')

delivery {
    apply plugin: "flow"
    apply plugin: "kkb_k8s_mos_deploy"

    config {
        category = "scrm"
        name = "kkb-scrm-dts-connector"
        release_repo  = "scrm/kkb-scrm-dts-connector"
        newArch = false
        subdir = ""
        version = "1.0.0-${env.COMMIT_ID}"
        k8s_namespaces = ["dev": "kkb-dev","test":"kkb-test","prod":"mos-prod"]
    }
}