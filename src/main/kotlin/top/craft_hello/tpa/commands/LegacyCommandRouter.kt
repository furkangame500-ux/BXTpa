package top.craft_hello.tpa.commands

import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import top.craft_hello.tpa.TPA
import top.craft_hello.tpa.objects.ConfigManager
import top.craft_hello.tpa.objects.LanguageManager
import top.craft_hello.tpa.objects.PlayerDataManager
import top.craft_hello.tpa.utils.SafeGuard
import top.craft_hello.tpa.utils.SendMessageUtil

// 1.8.8+ 传统命令路由：plugin.yml 声明的命令统一分发到各命令对象的
// executeXxx(sender, args)（与 Brigadier 树共用同一份业务实现），并按规则补全。
// 本类不 import 任何 Brigadier/Paper 高版本 API，低版本服务器可安全加载。
object LegacyCommandRouter : CommandExecutor, TabCompleter {

    fun register(plugin: TPA) {
        for (name in plugin.description.commands.keys) {
            val command = plugin.getCommand(name) ?: continue
            command.executor = this
            command.tabCompleter = this
        }
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val argList = args.toList()
        SafeGuard.commandLegacy(sender) {
            when (command.name.lowercase()) {
                "tpa" -> TpaCommand.executeTpa(sender, argList)
                "tphere" -> TphereCommand.executeTphere(sender, argList)
                "tpaccept" -> TpacceptCommand.executeTpaccept(sender, argList)
                "tpdeny" -> TpdenyCommand.executeTpdeny(sender, argList)
                else -> SendMessageUtil.syntaxGenericError(sender, command.name)
            }
        }
        return true
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>): List<String>? {
        val input = (args.lastOrNull() ?: "").lowercase()
        return if ((command.name.equals("tpa", true) || command.name.equals("tphere", true)) && args.size == 1) {
            val self = sender as? Player
            Bukkit.getOnlinePlayers().map { it.name }
                .filter { (self == null || it != self.name) && it.lowercase().contains(input) }
        } else emptyList()
    }
}
